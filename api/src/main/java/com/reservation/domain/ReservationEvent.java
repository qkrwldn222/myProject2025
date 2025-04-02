package com.reservation.domain;

import com.reservation.common.enums.ReservationEventStatus;
import com.reservation.common.enums.ReservationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEvent {
  private ReservationEventStatus reservationEventStatus; // CREATED, CANCELLED, COMPLETED, REVIEWED
  private Long reservationId;
  private Long restaurantId;
  private Long userId;
  private ReservationStatus reservationStatus; // PENDING, CONFIRMED, CANCELLED, COMPLETED
  private LocalDateTime timestamp;
}
