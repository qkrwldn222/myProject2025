package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantSeat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantSeatJpaRepository extends JpaRepository<RestaurantSeat, Long> {
  @Override
  Optional<RestaurantSeat> findById(Long id);
}
