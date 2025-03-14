package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.domain.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantImageJpaRepository extends JpaRepository<RestaurantImage, Long> {}
