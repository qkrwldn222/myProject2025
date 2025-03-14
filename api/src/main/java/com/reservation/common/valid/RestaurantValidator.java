package com.reservation.common.valid;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.ReservationOpenType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RestaurantValidator {
  public void validateReservationDays(
      List<String> reservationOpenDays,
      Map<String, String> reservationAvailableDays,
      ReservationOpenType openType) {
    if (openType == ReservationOpenType.ANYTIME) {
      return;
    }

    if (openType == ReservationOpenType.WEEKLY) {
      validateWeekly(reservationOpenDays);
    } else if (openType == ReservationOpenType.MONTHLY) {
      validateMonthly(reservationOpenDays);
    }

    validateAvailableDaysFormat(reservationAvailableDays);
  }

  private void validateWeekly(List<String> openDays) {
    if (CollectionUtils.isEmpty(openDays)) {
      throw new ApiException("예약 가능 요일을 최소 1개 입력해야 합니다.");
    }
    List<String> validDays = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    for (String day : openDays) {
      if (!validDays.contains(day)) {
        throw new ApiException("예약 신청 가능 요일이 잘못되었습니다.");
      }
    }
  }

  private void validateMonthly(List<String> openDays) {
    if (CollectionUtils.isEmpty(openDays)) {
      throw new ApiException("예약 신청 가능한 날짜를 입력해주세요.");
    }
    for (String day : openDays) {
      int date;
      try {
        date = Integer.parseInt(day);
      } catch (NumberFormatException e) {
        throw new ApiException("예약 신청 가능한 날짜는 숫자로 입력해주세요.");
      }
      if (date < 1 || date > 31) {
        throw new ApiException("예약 신청 가능 날짜는 1~31 사이여야 합니다.");
      }
    }
  }

  private void validateAvailableDaysFormat(Map<String, String> availableDays) {
    if (availableDays == null
        || !availableDays.containsKey("minDays")
        || !availableDays.containsKey("maxDays")) {
      throw new ApiException("예약 가능일 설정(minDays, maxDays)을 반드시 입력해주세요.");
    }

    int minDays = Integer.parseInt(availableDays.get("minDays"));
    int maxDays = Integer.parseInt(availableDays.get("maxDays"));

    if (minDays < 0 || maxDays < minDays) {
      throw new ApiException("예약 가능일 설정이 잘못되었습니다. (minDays ≥ 0, maxDays ≥ minDays)");
    }
  }
}
