package com.reservation.adapter.reservation.controller;

import com.reservation.adapter.reservation.mapper.ReservationRequestMapper;
import com.reservation.adapter.reservation.mapper.ReservationResponseMapper;
import com.reservation.adapter.reservation.model.ReservationRequest;
import com.reservation.adapter.reservation.model.RestaurantReservationResponse;
import com.reservation.adapter.reservation.swagger.ReservationSwagger;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.model.ReservationSearchCommand;
import com.reservation.application.reservation.service.ReservationService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import com.reservation.domain.Reservation;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("reservations")
@EnableGlobalExceptionHandling
@RequiredArgsConstructor
public class ReservationController implements ReservationSwagger {

  private final ReservationService reservationService;

  @Override
  public ResponseEntity<ApiResponseWrapper<Long>> createReservation(
      @RequestBody ReservationRequest request) {
    ReservationRequestCommand command = ReservationRequestMapper.INSTANCE.toCommand(request);

    Reservation reservation = reservationService.createReservation(command);

    return ResponseEntity.ok(
        ApiResponseWrapper.createResult("예약이 생성되었습니다.", reservation.getReservationId()));
  }

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> cancelReservation(
      @PathVariable("reservationId") Long reservationId) {
    reservationService.cancelReservation(reservationId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("예약이 취소되었습니다."));
  }

  @Override
  @PostMapping("/{reservationId}/confirm")
  public ResponseEntity<ApiResponseWrapper<Void>> confirmReservation(
      @PathVariable("reservationId") Long reservationId) {

    reservationService.confirmReservation(reservationId);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("예약 승인이 완료되었습니다."));
  }

  @Override
  @GetMapping("/search")
  public ResponseEntity<ApiListResponse<RestaurantReservationResponse>> searchReservations(
      @RequestParam(value = "userId", required = false) String userId,
      @RequestParam(value = "restaurant_id", required = false) Long restaurant_id,
      @RequestParam(value = "reservationStatus", required = false)
          ReservationStatus reservationStatus,
      @RequestParam(value = "reservationStartDate", required = false) LocalDate reservationStarDate,
      @RequestParam(value = "reservationEndDate", required = false) LocalDate reservationEndDate) {

    ReservationSearchCommand command =
        ReservationRequestMapper.INSTANCE.toCommand(
            userId, restaurant_id, reservationStatus, reservationStarDate, reservationEndDate);

    List<Reservation> reservations = reservationService.findReservations(command);

    List<RestaurantReservationResponse> result =
        ReservationResponseMapper.INSTANCE.toReservationResponses(reservations);

    return ResponseEntity.ok(ApiListResponse.multiResult("조회 성공", result));
  }
}
