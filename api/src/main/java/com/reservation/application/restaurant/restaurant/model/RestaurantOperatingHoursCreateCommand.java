package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.DayOfWeekEnum;
import com.reservation.common.valid.BaseValidation;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursCreateCommand implements BaseValidation {
  private DayOfWeekEnum dayOfWeek;
  private LocalTime openTime;
  private LocalTime closeTime;
  private Boolean isHoliday;
  private int reservationInterval;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(dayOfWeek)) {
      throw new ApiException("요일은 필수 값 입니다.");
    }
    if (ObjectUtils.isEmpty(openTime)) {
      throw new ApiException("오픈시간은 필수 값 입니다.");
    }
    if (ObjectUtils.isEmpty(closeTime)) {
      throw new ApiException("종료시간은 필수 값 입니다.");
    }
  }
}
