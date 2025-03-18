package com.reservation.application.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantMenu;
import java.util.List;
import java.util.Optional;

public interface RestaurantMenuJpaRepository {
  List<RestaurantMenu> findAllByRestaurantId(Long restaurantId);

  void saveAll(List<RestaurantMenu> menus);

  Optional<RestaurantMenu> findById(Long id);

  void deleteById(Long id);

  void deleteAllById(List<Long> ids);
}
