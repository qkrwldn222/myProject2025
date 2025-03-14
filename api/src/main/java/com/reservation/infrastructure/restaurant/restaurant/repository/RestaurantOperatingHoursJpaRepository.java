package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOperatingHoursJpaRepository
    extends JpaRepository<RestaurantOperatingHours, Long> {}
