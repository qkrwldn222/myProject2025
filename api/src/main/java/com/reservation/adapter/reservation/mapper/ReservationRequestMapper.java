package com.reservation.adapter.reservation.mapper;

import com.reservation.adapter.reservation.model.ReservationRequest;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.model.ReservationSearchCommand;
import com.reservation.common.enums.ReservationStatus;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationRequestMapper {
  ReservationRequestMapper INSTANCE = Mappers.getMapper(ReservationRequestMapper.class);

  ReservationRequestCommand toCommand(ReservationRequest request);

  ReservationSearchCommand toCommand(
      String userId,
      Long restaurant_id,
      ReservationStatus reservationStatus,
      LocalDate reservationStartDate,
      LocalDate reservationEndDate);
}
