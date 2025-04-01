package com.reservation.application.reservation.service;

import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.model.ReservationSearchCommand;
import com.reservation.domain.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

  /**
   * 예약 취소
   *
   * @param reservationId 예약 ID
   */
  void cancelReservation(Long reservationId);

  /**
   * 식당 예약
   *
   * @param command 예약 요청 커맨드
   * @return 예약 도메인
   */
  Reservation createReservation(ReservationRequestCommand command);

  /**
   * 예약 확정
   *
   * @param reservationId 가게 ID
   */
  void confirmReservation(Long reservationId);

  /**
   * @param command 조회 command
   * @return reservation 예약
   */
  List<Reservation> findReservations(ReservationSearchCommand command);

  Optional<Reservation> findById(Long reservationId);
}
