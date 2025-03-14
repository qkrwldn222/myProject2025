package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantMenuDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantOperatingHoursDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantSeatDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailResponse {
    private Long restaurantId;
    private String name;
    private String category;
    private String phoneNumber;
    private String address;
    private RegistrationStatus status;
    private BigDecimal depositAmount;
    private String reservationAvailableDays;
    private ReservationOpenType reservationOpenType;
    private String reservationOpenDays;
    private String specialBookingDays;
    private List<RestaurantMenuDTO> menus;
    private List<RestaurantOperatingHoursDTO> operatingHours;
    private List<RestaurantSeatDTO> seats;
}
