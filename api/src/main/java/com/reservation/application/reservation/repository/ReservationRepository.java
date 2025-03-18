package com.reservation.application.reservation.repository;

import com.reservation.common.enums.ReservationStatus;
import com.reservation.domain.Reservation;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantSeat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

public interface ReservationRepository {
  List<Reservation> findByRestaurantAndReservationDate(Restaurant restaurant, LocalDate date);

  List<Reservation> findAllByStatusAndExpiresAtBefore(
      ReservationStatus status, LocalDateTime localDateTime);

  boolean existsBySeatAndReservationDateAndReservationTime(
      RestaurantSeat seat, LocalDate reservationDate, LocalTime reservationTime);

  Optional<Reservation> findById(Long id);

  Reservation save(Reservation reservation);

  void deleteById(Long id);

  List<Reservation> findBySpecification(Specification<Reservation> spec);
}
