package com.reservation.infrastructure.kafka.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessage {
  private Long reviewId;
  private Long userId;
  private Long restaurantId;
  private Long reservationId;
  private Long waitingId;
  private String reviewText;
  private List<String> imageUrls;
}
