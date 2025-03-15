package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantSeat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantSeatJpaRepository extends JpaRepository<RestaurantSeat, Long> {

  Optional<RestaurantSeat> findBySeatId(Long id);

  Optional<RestaurantSeat> findBySeatIdAndRestaurantId(Long id, Long restaurantId);

  List<RestaurantSeat> findAllByRestaurantId(Long restaurantId);
}
