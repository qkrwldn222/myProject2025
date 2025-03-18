package com.reservation.application.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantOperatingHours;
import java.util.List;
import java.util.Optional;

public interface RestaurantOperatingJpaRepository {

  List<RestaurantOperatingHours> findAllByRestaurantId(Long restaurantId);

  void saveAll(List<RestaurantOperatingHours> restaurantOperatingHours);

  Optional<RestaurantOperatingHours> findById(Long id);

  void deleteAllById(List<Long> ids);
}
