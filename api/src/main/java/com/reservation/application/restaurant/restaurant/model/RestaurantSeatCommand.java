package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.enums.SeatType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSeatCommand {
  private SeatType seatType;
  private String seatNumber;
  private Integer maxCapacity;
  private Boolean isAvailable;
}
