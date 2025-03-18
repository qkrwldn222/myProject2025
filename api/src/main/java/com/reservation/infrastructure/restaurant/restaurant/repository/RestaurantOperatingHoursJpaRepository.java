package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantOperatingHours;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOperatingHoursJpaRepository
    extends JpaRepository<RestaurantOperatingHours, Long> {

  List<RestaurantOperatingHours> findAllByRestaurantId(Long restaurantId);
}
