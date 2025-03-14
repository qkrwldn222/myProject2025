package com.reservation.infrastructure.restaurant.restaurant.model;

import com.reservation.common.enums.SeatType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSeatDTO {
    private Long seatId;
    private Long restaurantId;
    private SeatType seatType;
    private String seatNumber;
    private Integer maxCapacity;
    private Boolean isAvailable;
}
