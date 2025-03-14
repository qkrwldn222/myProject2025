package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRegistrationJpaRepository
    extends JpaRepository<RestaurantRegistration, Long> {

    Optional<RestaurantRegistration> findByRestaurantId(Long restaurant_id);
}
