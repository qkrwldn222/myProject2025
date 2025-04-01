package com.reservation.application.review.service;

import com.reservation.application.review.model.ReviewSaveCommand;
import com.reservation.application.review.model.ReviewSearchCommand;
import com.reservation.domain.Review;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

  void saveReview(ReviewSaveCommand command, List<MultipartFile> images);

  void saveReview(ReviewSaveCommand command);

  List<String> saveImage(List<MultipartFile> images);

  void deleteReview(Long reviewId);

  List<Review> searchReviews(ReviewSearchCommand command);
}
