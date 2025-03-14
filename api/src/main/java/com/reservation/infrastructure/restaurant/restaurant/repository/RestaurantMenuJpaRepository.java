package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantMenuJpaRepository extends JpaRepository<RestaurantMenu, Long> {}
