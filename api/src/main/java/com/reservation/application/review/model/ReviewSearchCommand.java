package com.reservation.application.review.model;

import com.reservation.common.enums.SortOrder;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewSearchCommand {
  Long userId;
  Long restaurantId;
  LocalDate startDate;
  LocalDate endDate;
  SortOrder sortOrder;
}
