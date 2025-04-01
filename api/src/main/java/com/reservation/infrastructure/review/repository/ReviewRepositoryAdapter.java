package com.reservation.infrastructure.review.repository;

import com.reservation.application.review.repository.ReviewRepository;
import com.reservation.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryAdapter implements ReviewRepository {

  private final ReviewJpaRepository reviewJpaRepository;

  @Override
  public void saveReview(Review review) {
    reviewJpaRepository.save(review);
  }
}
