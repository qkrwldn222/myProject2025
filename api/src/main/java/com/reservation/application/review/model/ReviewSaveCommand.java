package com.reservation.application.review.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewSaveCommand {
  private Long userId;
  private Long restaurantId;
  private Long reservationId; // 예약 리뷰일 경우
  private Long waitingId; // 웨이팅 리뷰일 경우
  private String reviewText;

  private List<String> urls;
}
