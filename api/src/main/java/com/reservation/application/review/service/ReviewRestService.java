package com.reservation.application.review.service;

import com.reservation.application.reservation.service.ReservationService;
import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.review.mapper.ReviewAppRequestMapper;
import com.reservation.application.review.model.ReviewSaveCommand;
import com.reservation.application.review.model.ReviewSearchCommand;
import com.reservation.application.review.repository.ReviewRepository;
import com.reservation.application.waiting.service.WaitingService;
import com.reservation.common.config.ApiException;
import com.reservation.common.service.LocalFileStorageService;
import com.reservation.domain.*;
import com.reservation.infrastructure.kafka.KafkaEventPublisher;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewRestService implements ReviewService {

  private final LocalFileStorageService localFileStorageService;
  private final ReservationService reservationService;
  private final RestaurantService restaurantService;
  private final WaitingService waitingService;
  private final ReviewRepository reviewRepository;
  private final KafkaEventPublisher eventPublisher;
  private final String FILE_UPLOAD_PATH = "review";

  @Override
  public void saveReview(ReviewSaveCommand command, List<MultipartFile> images) {
    Restaurant restaurant =
        restaurantService
            .findById(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("레스토랑을 찾을 수 없습니다."));

    Optional<Reservation> reservation = reservationService.findById(command.getRestaurantId());
    Optional<WaitingHistory> waitingHistory = waitingService.findById(command.getRestaurantId());

    List<ReviewImage> reviews = new ArrayList<>();
    for (MultipartFile image : images) {
      String url = localFileStorageService.uploadImage(image, FILE_UPLOAD_PATH);
      ReviewImage build = ReviewImage.builder().imageUrl(url).build();
      reviews.add(build);
    }

    ReviewAppRequestMapper.INSTANCE.toEntity(
        command, reservation, restaurant, waitingHistory, reviews);
  }

  @Override
  public void deleteReview(Long reviewId) {}

  @Override
  public List<Review> searchReviews(ReviewSearchCommand command) {
    return List.of();
  }

  @Override
  public List<String> saveImage(List<MultipartFile> images) {
    List<String> resultList = new ArrayList<>();
    for (MultipartFile image : images) {
      String url = localFileStorageService.uploadImage(image, FILE_UPLOAD_PATH);
      resultList.add(url);
    }
    return resultList;
  }

  @Override
  @Transactional
  public void saveReview(ReviewSaveCommand command) {
    Restaurant restaurant =
        restaurantService
            .findById(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("레스토랑을 찾을 수 없습니다."));

    Optional<Reservation> reservation =
        Optional.ofNullable(command.getReservationId()).flatMap(reservationService::findById);

    Optional<WaitingHistory> waitingHistory =
        Optional.ofNullable(command.getWaitingId()).flatMap(waitingService::findById);

    List<ReviewImage> reviews = new ArrayList<>();

    if (reservation.isEmpty() && waitingHistory.isEmpty())
      throw new ApiException("리뷰를 작성할 수 없습니다.");

    Review entity =
        ReviewAppRequestMapper.INSTANCE.toEntity(
            command, reservation, restaurant, waitingHistory, reviews);

    for (String url : command.getUrls()) {
      ReviewImage build = ReviewImage.builder().imageUrl(url).review(entity).build();
      reviews.add(build);
    }
    entity.getReviewImages().addAll(reviews);

    reviewRepository.saveReview(entity);

    eventPublisher.publishReviewCreated(entity);
  }
}
