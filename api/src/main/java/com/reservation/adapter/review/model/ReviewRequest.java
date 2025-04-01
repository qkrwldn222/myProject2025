package com.reservation.adapter.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
  private Long userId;
  private Long restaurantId;
  private Long reservationId; // 예약 리뷰일 경우
  private Long waitingId; // 웨이팅 리뷰일 경우
  private String reviewText;
}
