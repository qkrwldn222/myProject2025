package com.reservation.application.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.model.RestaurantRegistrationSearchCommand;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantMenuDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantOperatingHoursDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantRegistrationDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantSeatDTO;

import java.util.List;

public interface RestaurantRepository {
    List<RestaurantRegistrationDTO> searchRegistrations(RestaurantRegistrationSearchCommand command);

    List<RestaurantMenuDTO> findMenusByRestaurantId(List<Long> restaurantIds);

    List<RestaurantOperatingHoursDTO> findOperatingHoursByRestaurantId( List<Long> restaurantIds);

    List<RestaurantSeatDTO> findSeatsByRestaurantId( List<Long> restaurantIds);
}
