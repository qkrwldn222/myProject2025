package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.DayOfWeekEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursUpdateRequest {
  @Schema(description = "운영 시간 ID (수정할 항목의 고유 ID)", example = "10")
  private Long operatingHoursId;

  @Schema(
      description = "요일",
      example = "FRI",
      allowableValues = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"})
  private DayOfWeekEnum dayOfWeek;

  @Schema(description = "영업 시작 시간 (HH:mm 형식)", example = "10:00")
  private LocalTime openTime;

  @Schema(description = "영업 종료 시간 (HH:mm 형식)", example = "23:00")
  private LocalTime closeTime;

  @Schema(description = "휴무 여부 (true: 휴무, false: 영업)", example = "false")
  private Boolean isHoliday;
}
