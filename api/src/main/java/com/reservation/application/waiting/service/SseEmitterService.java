package com.reservation.application.waiting.service;

import com.reservation.common.utils.RedisKeyUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseEmitterService {
  private static final Logger logger = LoggerFactory.getLogger(SseEmitterService.class);
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final RedisTemplate<String, String> redisTemplate;

  public SseEmitter subscribe(String userId, Long restaurantId) {
    String key = createEmitterKey(userId, restaurantId);
    SseEmitter emitter = new SseEmitter(180_000L);
    emitters.put(key, emitter);

    emitter.onCompletion(() -> emitters.remove(key));
    emitter.onTimeout(() -> emitters.remove(key));

    // 현재 대기열에서 내가 지금 조인하면 몇 번째인지 계산
    String queueKey = RedisKeyUtil.createWaitingQueueKey(restaurantId);

    List<String> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);

    int estimatedPosition = (waitingUsers == null ? 0 : waitingUsers.size()) + 1;

    try {
      emitter.send(
          SseEmitter.event().name("ESTIMATED_POSITION").data("POSITION : " + estimatedPosition));
    } catch (IOException e) {
      emitters.remove(key);
    }

    return emitter;
  }

  public void sendEvent(String userId, String event, Long restaurantId) {
    String key = createEmitterKey(userId, restaurantId);
    SseEmitter emitter = emitters.get(key);

    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("waitingUpdate").data(event));
      } catch (IOException e) {
        emitters.remove(key);
      }
    }
  }

  public List<String> getSubscribedUsers(Long restaurantId) {
    return emitters.keySet().stream()
        .filter(key -> key.endsWith(":" + restaurantId))
        .map(key -> key.split(":")[0])
        .collect(Collectors.toList());
  }

  private String createEmitterKey(String userId, Long restaurantId) {
    return userId + ":" + restaurantId;
  }

  public Set<String> getAllSubscribedUsers() {
    return emitters.keySet().stream()
        .map(key -> key.split(":")[0]) // userId만 추출
        .collect(Collectors.toSet());
  }

  //  @Scheduled(fixedRate = 30000) // 30초마다 실행
  public void keepAliveSSE() {
    for (String key : emitters.keySet()) {
      SseEmitter emitter = emitters.get(key);
      try {
        emitter.send(SseEmitter.event().name("KEEP_ALIVE").data("KEEP_ALIVE"));
      } catch (IOException e) {
        emitters.remove(key);
      }
    }
  }

  public void completeEmitter(String userId, Long restaurantId) {
    String key = createEmitterKey(userId, restaurantId);
    SseEmitter emitter = emitters.get(key);

    if (emitter != null) {
      emitter.complete(); // SSE 종료
      emitters.remove(key);
      logger.info("SSE 종료: {}", key);
    }
  }

  public void clearEmittersByRestaurant(Long restaurantId) {
    // 해당 레스토랑과 관련된 모든 SSE 키 찾기
    List<String> keysToRemove =
        emitters.keySet().stream()
            .filter(key -> key.endsWith(":" + restaurantId))
            .collect(Collectors.toList());

    // 해당 키에 대한 SSE 종료 및 삭제
    for (String key : keysToRemove) {
      SseEmitter emitter = emitters.get(key);
      if (emitter != null) {
        emitter.complete(); // SSE 종료
      }
      emitters.remove(key);
    }

    logger.info("레스토랑 ID {}의 모든 SSE 연결을 종료했습니다.", restaurantId);
  }
}
