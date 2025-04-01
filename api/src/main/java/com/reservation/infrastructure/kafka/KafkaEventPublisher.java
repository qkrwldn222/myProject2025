package com.reservation.infrastructure.kafka;

import com.reservation.domain.ReservationEvent;
import com.reservation.domain.Review;
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
  private final KafkaTemplate<String, Review> reviewKafkaTemplate;

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

  public void publishReviewCreated(Review review) {
    reviewKafkaTemplate
        .send("reservation.reviewed", review)
        .whenComplete(
            (result, ex) -> {
              if (ex == null) {
                log.info("Kafka 메시지 전송 성공: {}", review);
              } else {
                log.error("Kafka 메시지 전송 실패: {}", ex.getMessage());
              }
            });
    ;
  }
}
