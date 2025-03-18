package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantSpringDataJpaRepository extends JpaRepository<Restaurant, Long> {
  Optional<Restaurant> findById(Long id);

  Optional<Restaurant> findByOwnerId(Long ownerId);
}
