package com.reservation.infrastructure.restaurant.restaurant.model;

import com.reservation.common.enums.DayOfWeekEnum;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursDTO {
  private Long operatingHoursId;
  private Long restaurantId;
  private DayOfWeekEnum dayOfWeek;
  private LocalTime openTime;
  private LocalTime closeTime;
  private Boolean isHoliday;
}
