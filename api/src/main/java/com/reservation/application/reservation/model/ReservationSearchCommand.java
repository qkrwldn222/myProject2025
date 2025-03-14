package com.reservation.application.reservation.model;

import com.reservation.common.enums.ReservationStatus;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSearchCommand {
  String userId;
  Long restaurant_id;
  ReservationStatus reservationStatus;
  LocalDate reservationStarDate;
  LocalDate reservationEndDate;
}
