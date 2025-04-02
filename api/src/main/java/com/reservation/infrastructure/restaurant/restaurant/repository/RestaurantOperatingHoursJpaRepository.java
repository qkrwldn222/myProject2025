package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.common.enums.DayOfWeekEnum;
import com.reservation.domain.RestaurantOperatingHours;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOperatingHoursJpaRepository
    extends JpaRepository<RestaurantOperatingHours, Long> {

  List<RestaurantOperatingHours> findAllByRestaurantId(Long restaurantId);

  Optional<RestaurantOperatingHours> findByRestaurantIdAndDayOfWeek(
      Long restaurantId, DayOfWeekEnum dayOfWeekEnum);
}
