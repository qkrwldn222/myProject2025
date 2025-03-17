package com.reservation.application.waiting;


import com.reservation.adapter.security.config.CustomUserDetails;
import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.user.service.UserService;
import com.reservation.application.waiting.service.SseEmitterService;
import com.reservation.application.waiting.service.WaitingRestService;
import com.reservation.application.waiting.service.WaitingService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.*;
import com.reservation.common.utils.RedisKeyUtil;
import com.reservation.domain.*;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantMenuJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantOperatingHoursJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantSeatJpaRepository;
import com.reservation.infrastructure.role.repository.RoleJpaRepository;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest
public class WaitingServiceTest {

    private WaitingRestService waitingService;
    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;
    private SseEmitterService sseEmitterService;
    private UserService userService;
    private RestaurantService restaurantService;

    private User testUser1, testUser2, testOwner;
    private Restaurant testRestaurant;

    private static final Long TEST_RESTAURANT_ID = 1L;
    private static final Long TEST_OWNER_ID = 1L;
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";
    private static final String OWNER = "owner_user";

    @BeforeEach
    void setUp() {
        redisTemplate = Mockito.mock(RedisTemplate.class);
        listOperations = Mockito.mock(ListOperations.class);
        sseEmitterService = Mockito.spy(new SseEmitterService());
        userService = Mockito.mock(UserService.class);
        restaurantService = Mockito.mock(RestaurantService.class);
        waitingService = new WaitingRestService(redisTemplate, sseEmitterService, userService, restaurantService);

        // Redis ListOperations Mock 설정
        Mockito.doReturn(listOperations).when(redisTemplate).opsForList();

        // Mock User Data
        testOwner = User.builder()
                .userID(OWNER)
                .username("Owner User")
                .password("password")
                .build();

        testUser1 = User.builder()
                .userID(USER_1)
                .username("Test User 1")
                .password("password")
                .build();

        testUser2 = User.builder()
                .userID(USER_2)
                .username("Test User 2")
                .password("password")
                .build();

        // 빌더 패턴을 활용한 Restaurant 객체 생성
        testRestaurant = Restaurant.builder()
                .name("테스트 레스토랑")
                .category("한식")
                .phoneNumber("02-1234-5678")
                .address("서울시 강남구 테헤란로 123")
                .reservationOpenType(ReservationOpenType.ANYTIME)
                .reservationAvailableDays("{\"minDays\":1,\"maxDays\":30}")
                .status(RegistrationStatus.APPROVED)
                .managementStatus(RestaurantStatus.OPEN)
                .ownerId(testOwner.getId()) // 오너 ID 설정
                .autoConfirm(true)
                .approvalTimeout(60)
                .depositAmount(new BigDecimal("10000.00"))
                .build();

        // Mock 데이터 리턴 설정
        Mockito.when(userService.findById(testOwner.getId())).thenReturn(Optional.of(testOwner));
        Mockito.when(userService.findByUserId(OWNER)).thenReturn(Optional.of(testOwner));
        Mockito.when(userService.findByUserId(USER_1)).thenReturn(Optional.of(testUser1));
        Mockito.when(userService.findByUserId(USER_2)).thenReturn(Optional.of(testUser2));
        Mockito.when(restaurantService.findById(TEST_RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
//        Mockito.when(sseEmitterService.createEmitterKey(USER_1 , TEST_RESTAURANT_ID)).thenReturn(testUser1.getUserID()+":"+ TEST_RESTAURANT_ID);

    }

    /**
     * GIVEN: 사용자가 웨이팅 대기열에 참여
     * WHEN: joinWaitingQueue 호출
     * THEN: 대기열에 사용자 추가됨
     */
    @Test
    @WithUserDetails(USER_1)
    void testJoinWaitingQueue() {
        // Given
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);

        // Spy를 사용하여 실제 로직을 유지하면서 특정 메서드만 Stub 처리
        sseEmitterService = Mockito.spy(new SseEmitterService());

        // sendEvent()가 실행될 때 아무 작업도 하지 않도록 Stub 처리
        Mockito.doNothing().when(sseEmitterService)
                .sendEvent(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        // When
        waitingService.joinWaitingQueue(TEST_RESTAURANT_ID);

        // Then
        Mockito.verify(listOperations).rightPush(queueKey, USER_1);

        // `sendEvent()` 호출을 기다렸다가 검증
        Awaitility.await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(sseEmitterService).sendEvent(USER_1, "POSITION : 1", TEST_RESTAURANT_ID);
        });
    }

    /**
     * GIVEN: 사용자가 대기열에서 나감
     * WHEN: leaveWaitingQueue 호출
     * THEN: 대기열에서 사용자 제거됨
     */
    @Test
    @WithUserDetails(USER_1)
    void testLeaveWaitingQueue() {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);
        Mockito.when(listOperations.remove(queueKey, 1, USER_1)).thenReturn(1L);

        waitingService.leaveWaitingQueue(TEST_RESTAURANT_ID);

        Mockito.verify(listOperations).remove(queueKey, 1, USER_1);
        Mockito.verify(sseEmitterService).sendEvent(USER_1, "POSITION : -1", TEST_RESTAURANT_ID);
    }

    /**
     * GIVEN: 운영자가 다음 사용자 입장 요청
     * WHEN: allowNextUser 호출
     * THEN: 대기열 첫 번째 사용자에게 "ENTER" 알림 전송
     */
    @Test
    @WithUserDetails(OWNER)
    void testAllowNextUser() {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);
        Mockito.when(listOperations.index(queueKey, 0)).thenReturn(USER_1);

        waitingService.allowNextUser(TEST_RESTAURANT_ID);

        Mockito.verify(sseEmitterService).sendEvent(USER_1, "ENTER", TEST_RESTAURANT_ID);
    }

    /**
     * GIVEN: 운영자가 대기열에서 실제 입장 확정
     * WHEN: confirmEntry 호출
     * THEN: 대기열 첫 번째 사용자가 제거됨
     */
    @Test
    @WithUserDetails(OWNER)
    void testConfirmEntry() {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);
        Mockito.when(listOperations.leftPop(queueKey)).thenReturn(USER_1);

        waitingService.confirmEntry(TEST_RESTAURANT_ID);

//        Mockito.doNothing().when(sseEmitterService).sendEvent(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        Mockito.verify(listOperations).leftPop(queueKey);
        Mockito.verify(sseEmitterService).subscribe(USER_1,TEST_RESTAURANT_ID);
        Mockito.verify(sseEmitterService).sendEvent(USER_1, "ENTER", TEST_RESTAURANT_ID);


    }

    /**
     * GIVEN: 대기열에 사용자가 존재
     * WHEN: getWaitingList 호출
     * THEN: 대기 중인 사용자 ID 목록 반환
     */
    @Test
    void testGetWaitingList() {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);
        List<String> mockList = List.of(USER_1, USER_2);

        Mockito.when(listOperations.range(queueKey, 0, -1)).thenReturn(mockList);

        List<String> result = waitingService.getWaitingList(TEST_RESTAURANT_ID);

        assertEquals(mockList, result);
    }

    /**
     * GIVEN: 특정 사용자가 대기 중
     * WHEN: getUserWaitingPosition 호출
     * THEN: 현재 대기 순서 반환
     */
    @Test
    void testGetUserWaitingPosition() {
        String queueKey = RedisKeyUtil.createWaitingQueueKey(TEST_RESTAURANT_ID);
        List<String> mockList = List.of(USER_1, USER_2);

        Mockito.when(listOperations.range(queueKey, 0, -1)).thenReturn(mockList);

        int position = waitingService.getUserWaitingPosition(TEST_RESTAURANT_ID, USER_1);
        int position2 = waitingService.getUserWaitingPosition(TEST_RESTAURANT_ID, USER_2);
        assertEquals(1, position);
        assertEquals(2, position2);
    }
}
