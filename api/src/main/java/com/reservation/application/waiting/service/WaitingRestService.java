package com.reservation.application.waiting.service;


import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.utils.RedisKeyUtil;
import com.reservation.common.utils.SecurityUtil;
import com.reservation.domain.Restaurant;
import com.reservation.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WaitingRestService implements WaitingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SseEmitterService sseEmitterService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    @Override
    public List<String> getWaitingList(Long restaurantId) {
        return List.of();
    }

    /**
     * 특정 레스토랑의 웨이팅 대기열에 사용자 추가
     * @param restaurantId 레스토랑 ID
     */
    @Override
    @Transactional
    public void joinWaitingQueue(Long restaurantId) {
        String currentUserId = SecurityUtil.getCurrentUserId();

        userService.findByUserId(currentUserId)
                .orElseThrow(() -> new ApiException("유효하지 않은 사용자입니다.", HttpStatus.BAD_REQUEST));

        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);

        // 중복 확인
        if (waitingUsers != null && waitingUsers.contains(currentUserId)) {
            throw new ApiException("이미 대기열에 존재하는 사용자입니다.", HttpStatus.CONFLICT);
        }

        // 대기열에 추가
        redisTemplate.opsForList().rightPush(queueKey, currentUserId);

        // 현재 대기열에서 내 앞에 있는 사람 수 계산 (내 위치 찾기)
        int myPosition = (waitingUsers == null ? 0 : waitingUsers.size()) + 1;
        sseEmitterService.sendEvent(currentUserId, "POSITION : " + myPosition, restaurantId);

        notifyWaitingUsersOnJoin(restaurantId, myPosition);
    }

    /**
     * 웨이팅 대기열에서 나가기 (본인만 가능)
     * @param restaurantId 레스토랑 ID
     */
    public void leaveWaitingQueue(Long restaurantId) {
        String currentUserId = SecurityUtil.getCurrentUserId();

        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);

        if (waitingUsers == null || !waitingUsers.contains(currentUserId)) {
            throw new ApiException("대기열에 없는 사용자입니다.", HttpStatus.BAD_REQUEST);
        }

        int myPosition = waitingUsers.indexOf(currentUserId);
        redisTemplate.opsForList().remove(queueKey, 1, currentUserId);

        // 내 앞 번호였던 사람이 나갔을 경우만 내 번호를 업데이트해야 함
        notifyWaitingUsersOnLeave(restaurantId, myPosition);
    }

    @Override
    public void leaveWaitingQueueByOwner(Long restaurantId) {
        String currentUserId = SecurityUtil.getCurrentUserId();

        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(() -> new ApiException("가게를 찾을 수 없습니다.",HttpStatus.BAD_REQUEST));

        User ownerUser = userService.findByUserId(currentUserId).orElseThrow(() -> new ApiException("유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));

        if(!ownerUser.getId().equals(restaurant.getOwnerId())) throw new ApiException("가게 운영자가 아닙니다.",HttpStatus.BAD_REQUEST);

        // 대기열 첫 번째 유저 가져오기
        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        String firstUserId = redisTemplate.opsForList().leftPop(queueKey);

        if (firstUserId == null) {
            throw new ApiException("현재 대기 중인 사용자가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 해당 유저가 SSE 구독 중이면 종료 처리
        sseEmitterService.completeEmitter(firstUserId, restaurantId);

        // 나머지 대기자들에게 순번 변경 알림
        notifyWaitingUsersOnLeave(restaurantId, 0);


        List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);
        int myPosition = (waitingUsers == null ? 0 : waitingUsers.size()) + 1;
        notifyWaitingUsersOnJoin(restaurantId, myPosition);
    }

    /**
     * 관리자가 다음 대기자를 입장 요청 (입장 가능 알림 전송)
     * @param restaurantId 레스토랑 ID
     */
    @Transactional
    public User allowNextUser(Long restaurantId) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        validateOwner(restaurantId, currentUserId);

        String nextUserID = getNextUser(restaurantId);
        if (nextUserID == null) {
            throw new ApiException("현재 대기 중인 사용자가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 맨 앞 사용자에게 "입장 가능" 알림 전송
        sseEmitterService.sendEvent(nextUserID, "ENTER", restaurantId);

        return userService.findByUserId(nextUserID)
                .orElseThrow(() -> new ApiException("유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    }

    /**
     * 관리자가 대기열에서 실제 입장을 확정
     * @param restaurantId 레스토랑 ID
     */
    @Override
    @Transactional
    public void confirmEntry(Long restaurantId) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        validateOwner(restaurantId, currentUserId);

        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);

        if (waitingUsers == null || waitingUsers.isEmpty()) {
            throw new ApiException("현재 입장 가능한 대기자가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 맨 앞 사용자 가져오기
        String firstUserId = waitingUsers.get(0);

        // "ENTER" 이벤트 전송 (첫 번째 사용자에게 입장 안내)
        sseEmitterService.sendEvent(firstUserId, "ENTER", restaurantId);
    }

    /**
     * 특정 사용자의 현재 대기 순서 반환
     * @param restaurantId 레스토랑 ID
     * @param userId 사용자 ID
     * @return 현재 대기 순서 (1부터 시작, 없으면 -1)
     */
    public int getUserWaitingPosition(Long restaurantId, String userId) {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);

        if (waitingUsers == null || waitingUsers.isEmpty()) {
            return -1;
        }
        int position = waitingUsers.indexOf(userId);
        return (position != -1) ? position + 1 : -1;
    }

    /**
     * 대기열 변경 시 전체 구독자에게 순번 변경 알림
     * @param restaurantId 레스토랑 ID
     */
    private void notifyAllWaitingUsers(Long restaurantId) {
        for (String userId : sseEmitterService.getSubscribedUsers(restaurantId)) {
            int position = getUserWaitingPosition(restaurantId, userId);
            sseEmitterService.sendEvent(userId, "POSITION : " + position, restaurantId);
        }
    }

    /**
     * 운영자 권한 검증
     * @param restaurantId 레스토랑 ID
     * @param userId 확인할 사용자 ID
     */
    private void validateOwner(Long restaurantId, String userId) {
        Restaurant restaurant = restaurantService.findById(restaurantId)
                .orElseThrow(() -> new ApiException("레스토랑을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        User owner = userService.findById(restaurant.getOwnerId())
                .orElseThrow(() -> new ApiException("가게 운영자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!owner.getUserID().equals(userId)) {
            throw new ApiException("해당 레스토랑의 운영자만 수행할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }

    // 대기열에서 다음 사용자 확인
    public String getNextUser(Long restaurantId) {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);
        return redisTemplate.opsForList().index(queueKey, 0);
    }

    private void notifyWaitingUsersOnLeave(Long restaurantId, Integer removedPosition) {
        if (removedPosition == null) return;

        // 현재 대기열 정보 가져오기
        List<String> waitingUsers = redisTemplate.opsForList().range(
                RedisKeyUtil.createWaitingQueueKey(restaurantId), 0, -1);

        if (waitingUsers == null || waitingUsers.isEmpty()) {
            return;
        }

        // 현재 구독 중인 사용자 목록
        Set<String> subscribedUsers = sseEmitterService.getAllSubscribedUsers();

        // 이전 순번과 새로운 순번을 매핑하여 저장할 맵
        Map<String, Integer> newPositions = new HashMap<>();

        // 대기열 전체를 순회하면서 새로운 순번 할당
        for (int i = 0; i < waitingUsers.size(); i++) {
            String userId = waitingUsers.get(i);
            newPositions.put(userId, i + 1); // 순번은 1부터 시작
        }

        // 변경된 순번을 실제 구독자에게만 전송
        for (Map.Entry<String, Integer> entry : newPositions.entrySet()) {
            String userId = entry.getKey();
            int newPosition = entry.getValue();

            if (subscribedUsers.contains(userId)) {
                sseEmitterService.sendEvent(userId, "POSITION : " + newPosition, restaurantId);
            }
        }
    }

    private void notifyWaitingUsersOnJoin(Long restaurantId, int newJoinPosition) {
        // 현재 구독 중인 사용자 목록 가져오기
        Set<String> subscribedUsers = sseEmitterService.getAllSubscribedUsers();

        // 현재 대기열 정보 가져오기
        List<String> waitingUsers = redisTemplate.opsForList().range(
                RedisKeyUtil.createWaitingQueueKey(restaurantId), 0, -1);

        if (waitingUsers == null || waitingUsers.isEmpty()) {
            return;
        }

        for (String userId : subscribedUsers) {
            if (!waitingUsers.contains(userId)) {
                // 단순 구독자는 현재 조인하면 몇 번째인지 보내줌
                sseEmitterService.sendEvent(userId, "ESTIMATED_POSITION : " + (newJoinPosition + 1), restaurantId);
            }
        }
    }
}
