package com.reservation.application.restaurant.restaurant.repository;

import com.reservation.domain.Restaurant;
import java.util.Optional;

public interface RestaurantJpaRepository {
  Optional<Restaurant> findById(Long id);

  Optional<Restaurant> findByOwnerId(Long ownerId);

  Restaurant save(Restaurant restaurant);
}
