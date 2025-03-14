package com.reservation.adapter.reservation.mapper;

import com.reservation.adapter.reservation.model.RestaurantReservationResponse;
import com.reservation.domain.Reservation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationResponseMapper {
  ReservationResponseMapper INSTANCE = Mappers.getMapper(ReservationResponseMapper.class);

  @Mapping(source = "reservation.reservationId", target = "reservationId")
  @Mapping(source = "restaurant.id", target = "restaurantId")
  @Mapping(source = "restaurant.name", target = "restaurantName")
  @Mapping(source = "restaurant.category", target = "restaurantCategory")
  @Mapping(source = "restaurant.phoneNumber", target = "restaurantPhoneNumber")
  @Mapping(source = "restaurant.address", target = "restaurantAddress")
  @Mapping(source = "seat.seatId", target = "seatId")
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "reservation.status", target = "reservationStatus")
  RestaurantReservationResponse toResponse(Reservation reservation);

  List<RestaurantReservationResponse> toReservationResponses(List<Reservation> reservations);
}
