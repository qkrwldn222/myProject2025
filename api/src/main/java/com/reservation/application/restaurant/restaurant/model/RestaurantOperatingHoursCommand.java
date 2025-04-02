package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.enums.DayOfWeekEnum;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursCommand {
  private DayOfWeekEnum dayOfWeek;
  private LocalTime openTime;
  private LocalTime closeTime;
  private Boolean isHoliday;
  private int reservationInterval;
}
