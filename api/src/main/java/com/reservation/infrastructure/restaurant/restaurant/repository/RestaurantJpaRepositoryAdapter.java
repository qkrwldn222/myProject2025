package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.repository.RestaurantJpaRepository;
import com.reservation.domain.Restaurant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantJpaRepositoryAdapter implements RestaurantJpaRepository {

  private final RestaurantSpringDataJpaRepository restaurantJpaRepository;

  @Override
  public Optional<Restaurant> findById(Long id) {
    return restaurantJpaRepository.findById(id);
  }

  @Override
  public Optional<Restaurant> findByOwnerId(Long ownerId) {
    return restaurantJpaRepository.findByOwnerId(ownerId);
  }

  @Override
  public Restaurant save(Restaurant restaurant) {
    return restaurantJpaRepository.save(restaurant);
  }
}
