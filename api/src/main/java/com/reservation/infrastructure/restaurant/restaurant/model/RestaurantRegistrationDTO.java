package com.reservation.infrastructure.restaurant.restaurant.model;

import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.RestaurantStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RestaurantRegistrationDTO {
    private Long restaurantId;
    private String name;
    private RegistrationStatus status;
    private RestaurantStatus  managementStatus;
    private String ownerId;
    private String category;
    private String phoneNumber;
    private String address;
    private BigDecimal depositAmount;
    private String reservationAvailableDays;
    private ReservationOpenType reservationOpenType;
    private String reservationOpenDays;
    private String specialBookingDays;


}
