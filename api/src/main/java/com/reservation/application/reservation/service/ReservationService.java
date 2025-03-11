package com.reservation.application.reservation.service;


import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.domain.Reservation;

import java.util.List;

public interface ReservationService {

    /**
     * 예약 취소
     * @param reservationId 예약 ID
     */
    void cancelReservation(Long reservationId);

    /**
     * 식당 예약
     * @param command 예약 요청 커맨드
     * @return 예약 도메인
     */
   Reservation createReservation(ReservationRequestCommand command);

}
