package com.reservation.application.usecase;

import com.reservation.common.enums.ReservationEventStatus;
import com.reservation.domain.Reservation;
import com.reservation.domain.ReservationEvent;
import com.reservation.infrastructure.kafka.KafkaEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventUseCase {

  private final KafkaEventPublisher eventPublisher;

  public void createReservation(Reservation reservation) {
    // Kafka 이벤트 발행
    ReservationEvent event =
        new ReservationEvent(
            ReservationEventStatus.CREATED,
            reservation.getReservationId(),
            reservation.getRestaurant().getId(),
            reservation.getUser().getId(),
            reservation.getStatus(),
            LocalDateTime.now());
    eventPublisher.sendReservationEvent("reservation.created", event);
  }

  public void handleReservationEvent(ReservationEvent event) {
    log.info("event 수신 : {}", event);
    // Kafka Consumer가 메시지를 받으면 해당 이벤트 처리
    switch (event.getReservationEventStatus()) {
      case CREATED -> log.info("CREATED");
      case REVIEWED -> log.info("REVIEW");
      case CANCELLED -> log.info("CANCEL");
      case COMPLETED -> log.info("COMPLETED");
    }
  }
}
