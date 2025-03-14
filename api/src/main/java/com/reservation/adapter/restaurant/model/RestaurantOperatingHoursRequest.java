package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursRequest {

  @Schema(description = "요일", example = "MON")
  private String dayOfWeek;

  @Schema(description = "영업 시작 시간", example = "10:00")
  private String openTime;

  @Schema(description = "영업 종료 시간", example = "22:00")
  private String closeTime;

  @Schema(description = "휴무 여부", example = "false")
  private Boolean isHoliday;
}
