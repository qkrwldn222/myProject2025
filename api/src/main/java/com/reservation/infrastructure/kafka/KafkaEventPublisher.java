package com.reservation.infrastructure.kafka;

import com.reservation.domain.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {
  private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

  private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

  public void sendReservationEvent(String topic, ReservationEvent event) {
    kafkaTemplate
        .send(topic, event)
        .whenComplete(
            (result, ex) -> {
              if (ex == null) {
                log.info("Kafka 메시지 전송 성공: {}", event);
              } else {
                log.error("Kafka 메시지 전송 실패: {}", ex.getMessage());
              }
            });
  }
}
