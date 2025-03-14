package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<Restaurant, Long> {
  Optional<Restaurant> findById(Long id);
}
