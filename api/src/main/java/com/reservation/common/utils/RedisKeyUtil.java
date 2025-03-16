package com.reservation.common.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public final class RedisKeyUtil {
  private static final String RESERVATION_PREFIX = "reservation:";
  private static final String USER_RESERVATION_PREFIX = "user-reservation:";

  private RedisKeyUtil() {
    // 인스턴스화 방지
  }

  public static String createSeatReservationKey(
      Long restaurantId, Long seatId, LocalDate date, LocalTime time) {
    return RESERVATION_PREFIX + restaurantId + ":" + seatId + ":" + date + ":" + time;
  }

  public static String createUserReservationKey(Long userId, LocalDate date, LocalTime time) {
    return USER_RESERVATION_PREFIX + userId + ":" + date + ":" + time;
  }

  public static String getUserReservationPrefix(String userId, LocalDate date) {
    return USER_RESERVATION_PREFIX + userId + ":" + date + ":*";
  }
}
