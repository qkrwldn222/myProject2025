package com.reservation.infrastructure.restaurant.restaurant.model;

import com.reservation.common.enums.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long restaurantId;
    private String name;
    private String category;
    private String phoneNumber;
    private String address;
    private RestaurantStatus status;
    private BigDecimal depositAmount;
}