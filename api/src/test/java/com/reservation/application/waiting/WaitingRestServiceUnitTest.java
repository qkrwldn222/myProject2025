package com.reservation.application.waiting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.user.service.UserService;
import com.reservation.application.waiting.service.SseEmitterService;
import com.reservation.application.waiting.service.WaitingRestService;
import com.reservation.infrastructure.waiting.repository.WaitingHistoryJpaRepositoryAdapter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class WaitingRestServiceUnitTest {

  @Mock private RedisTemplate<String, String> redisTemplate;
  @Mock private ListOperations<String, String> listOperations;
  @Mock private SseEmitterService sseEmitterService;
  @Mock private UserService userService;
  @Mock private RestaurantService restaurantService;
  @Mock private WaitingHistoryJpaRepositoryAdapter waitingHistoryJpaRepositoryAdapter;

  private WaitingRestService waitingRestService;

  @BeforeEach
  void setUp() {
    waitingRestService =
        new WaitingRestService(
            redisTemplate,
            sseEmitterService,
            userService,
            restaurantService,
            waitingHistoryJpaRepositoryAdapter);
    when(redisTemplate.opsForList()).thenReturn(listOperations);
  }

  @Test
  void getWaitingListReturnsUsersFromRedis() {
    when(listOperations.range("restaurant:10:waiting_queue", 0, -1))
        .thenReturn(List.of("user1", "user2"));

    List<String> waitingList = waitingRestService.getWaitingList(10L);

    assertEquals(List.of("user1", "user2"), waitingList);
  }

  @Test
  void getWaitingListReturnsEmptyWhenRedisIsEmpty() {
    when(listOperations.range("restaurant:10:waiting_queue", 0, -1)).thenReturn(null);

    List<String> waitingList = waitingRestService.getWaitingList(10L);

    assertTrue(waitingList.isEmpty());
  }
}
