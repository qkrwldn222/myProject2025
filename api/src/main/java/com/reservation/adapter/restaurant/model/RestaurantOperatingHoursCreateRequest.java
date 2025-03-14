package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.DayOfWeekEnum;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursCreateRequest {
  @Schema(description = "요일", example = "MON", allowableValues = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"})
  private DayOfWeekEnum dayOfWeek;

  @Schema(description = "영업 시작 시간 (HH:mm 형식)", example = "09:00")
  private LocalTime openTime;

  @Schema(description = "영업 종료 시간 (HH:mm 형식)", example = "21:00")
  private LocalTime closeTime;

  @Schema(description = "휴무 여부 (true: 휴무, false: 영업)", example = "false")
  private Boolean isHoliday;
}
