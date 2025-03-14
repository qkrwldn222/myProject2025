package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantRegistration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRegistrationJpaRepository
    extends JpaRepository<RestaurantRegistration, Long> {

  Optional<RestaurantRegistration> findByRestaurantId(Long restaurant_id);
}
