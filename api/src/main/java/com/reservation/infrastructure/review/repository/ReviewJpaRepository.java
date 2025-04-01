package com.reservation.infrastructure.review.repository;

import com.reservation.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {}
