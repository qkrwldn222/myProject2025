package com.reservation.infrastructure.reservation.repository;

import com.reservation.application.reservation.repository.ReservationRepository;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.domain.Reservation;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantSeat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationJpaRepositoryAdapter implements ReservationRepository {
  private final ReservationJpaRepository reservationJpaRepository;

  @Override
  public List<Reservation> findByRestaurantAndReservationDate(
      Restaurant restaurant, LocalDate date) {
    return reservationJpaRepository.findByRestaurantAndReservationDate(restaurant, date);
  }

  @Override
  public List<Reservation> findAllByStatusAndExpiresAtBefore(
      ReservationStatus status, LocalDateTime localDateTime) {
    return reservationJpaRepository.findAllByStatusAndExpiresAtBefore(status, localDateTime);
  }

  @Override
  public boolean existsBySeatAndReservationDateAndReservationTime(
      RestaurantSeat seat, LocalDate reservationDate, LocalTime reservationTime) {
    return reservationJpaRepository.existsBySeatAndReservationDateAndReservationTime(
        seat, reservationDate, reservationTime);
  }

  @Override
  public Optional<Reservation> findById(Long id) {
    return reservationJpaRepository.findById(id);
  }

  @Override
  public Reservation save(Reservation reservation) {
    return reservationJpaRepository.save(reservation);
  }

  @Override
  public void deleteById(Long id) {
    reservationJpaRepository.deleteById(id);
  }

  @Override
  public List<Reservation> findBySpecification(Specification<Reservation> spec) {
    return reservationJpaRepository.findAll(spec);
  }
}
