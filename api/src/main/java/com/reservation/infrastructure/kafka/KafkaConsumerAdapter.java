package com.reservation.infrastructure.kafka;

import com.reservation.application.usecase.ReservationEventUseCase;
import com.reservation.domain.ReservationEvent;
import com.reservation.infrastructure.kafka.model.ReviewMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerAdapter {

  private final ReservationEventUseCase reservationEventUseCase;
  private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

  private static final int MAX_RETRIES = 3; // 최대 재시도 횟수

  @KafkaListener(
      topics = {"reservation.created", "reservation.cancelled", "reservation.completed"},
      groupId = "notification-group")
  public void consumeMultipleTopics(ReservationEvent event) {
    try {
      log.info("Kafka 메시지 수신: {}", event);
      reservationEventUseCase.handleReservationEvent(event);
    } catch (Exception e) {
      log.error("Kafka Consumer 오류 발생: {}, 메시지를 DLQ로 이동", e.getMessage());

      // DLQ로 보낼 때 retry-count=0으로 초기화
      kafkaTemplate.send(
          MessageBuilder.withPayload(event)
              .setHeader(KafkaHeaders.TOPIC, "reservation.dlq")
              .setHeader("retry-count", 0)
              .build());
    }
  }

  @KafkaListener(
      topics = {"reservation.reviewed"},
      groupId = "notification-group")
  public void consumeReview(ReviewMessage message) {
    try {
      log.info("Kafka 메시지 수신: {}", message);
    } catch (Exception e) {
      log.error("Kafka Consumer 오류 발생: {}, 메시지를 DLQ로 이동", e.getMessage());

      // DLQ로 보낼 때 retry-count=0으로 초기화
      kafkaTemplate.send(
          MessageBuilder.withPayload(message)
              .setHeader(KafkaHeaders.TOPIC, "reservation.dlq")
              .setHeader("retry-count", 0)
              .build());
    }
  }

  @KafkaListener(topics = "reservation.dlq", groupId = "reservation-dlq-group")
  public void consumeDLQReservationEvent(
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, // 원래 메시지가 온 토픽
      @Header(name = "retry-count", required = false) Integer retryCount, // 현재 재시도 횟수
      ReservationEvent event) {

    retryCount = (retryCount == null) ? 0 : retryCount;

    log.info("[DLQ] {}에서 온 메시지 재처리 시도 ({}회차): {}", topic, retryCount + 1, event);

    try {
      Thread.sleep(5000); // 5초 후 재처리 시도
      reservationEventUseCase.handleReservationEvent(event);
      log.info(" [DLQ] {} 메시지 처리 성공: {}", topic, event);
    } catch (Exception e) {
      log.error(" [DLQ] {} 메시지 처리 실패: {}, 재시도 횟수: {}", topic, e.getMessage(), retryCount + 1);

      if (retryCount + 1 >= MAX_RETRIES) {
        log.error("최대 재시도 횟수 초과 메시지를 최종 실패 토픽으로 이동: {}", event);

        // 최종 실패한 메시지를 별도 토픽으로 이동하여 운영자가 확인할 수 있도록 함
        kafkaTemplate.send(
            MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "reservation.dlq.failed") // 최종 실패 토픽
                .setHeader("original-topic", topic) // 원래 메시지가 온 토픽 정보 저장
                .build());
        return; // 메시지 폐기
      }

      // 재시도 횟수를 1 증가시켜 다시 DLQ로 전송
      kafkaTemplate.send(
          MessageBuilder.withPayload(event)
              .setHeader(KafkaHeaders.TOPIC, "reservation.dlq")
              .setHeader("retry-count", retryCount + 1) // retry-count 증가
              .setHeader("original-topic", topic) // 원래 메시지가 온 토픽 정보 유지
              .build());
    }
  }
}
