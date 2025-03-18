package com.reservation.application.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantSeat;
import java.util.List;
import java.util.Optional;

public interface RestaurantSeatJpaRepository {
  Optional<RestaurantSeat> findById(Long id);

  Optional<RestaurantSeat> findBySeatIdAndRestaurantId(Long id, Long restaurantId);

  List<RestaurantSeat> findAllByRestaurantId(Long restaurantId);

  void saveAll(List<RestaurantSeat> restaurantSeats);

  void deleteAllById(List<Long> ids);
}
