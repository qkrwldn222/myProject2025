package com.reservation.infrastructure.reservation.repository;

import com.reservation.common.enums.ReservationStatus;
import com.reservation.domain.Reservation;
import com.reservation.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRestaurantAndReservationDate(Restaurant restaurant, LocalDate date);

    List<Reservation>findAllByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime localDateTime);

}
