package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.RestaurantStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantRegistrationSearchCommand {
    private String userId;
    private String name;
    private RestaurantStatus status;
    private RegistrationStatus managementStatus;
}