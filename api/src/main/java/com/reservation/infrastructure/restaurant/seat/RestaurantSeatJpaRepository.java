package com.reservation.infrastructure.restaurant.seat;

import com.reservation.domain.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantSeatJpaRepository extends JpaRepository<RestaurantSeat, Long> {
    @Override
    Optional<RestaurantSeat> findById(Long id);
}
