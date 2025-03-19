package com.reservation.infrastructure.kafka;

import com.reservation.domain.ReservationEvent;
import java.time.Duration;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationDLQBatchProcessor {
  private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;
  private final ConsumerFactory<String, ReservationEvent> consumerFactory;

  // 1시간마다 실행 (배치 복구)
  @Scheduled(fixedRate = 3600000)
  public void processDLQMessages() {
    log.info("[DLQ BATCH] reservation.dlq.failed 메시지 복구 시작...");

    // Kafka Consumer를 직접 생성하여 메시지 수동 Polling
    try (Consumer<String, ReservationEvent> consumer = consumerFactory.createConsumer()) {
      consumer.subscribe(Collections.singletonList("reservation.dlq.failed"));

      // Kafka에서 배치로 메시지 가져오기 (한 번에 최대 100개)
      ConsumerRecords<String, ReservationEvent> records = consumer.poll(Duration.ofSeconds(5));

      for (ConsumerRecord<String, ReservationEvent> record : records) {
        ReservationEvent event = record.value();
        String originalTopic =
            record.headers().lastHeader("original-topic") != null
                ? new String(record.headers().lastHeader("original-topic").value())
                : "reservation.created"; // 기본값

        log.info("[DLQ BATCH] 원래 토픽({})으로 복구 시도: {}", originalTopic, event);
        // 원래 토픽으로 복구
        kafkaTemplate.send(originalTopic, event);
      }
      // 메시지 커밋 (Kafka에서 읽은 것으로 처리)
      consumer.commitSync();
    } catch (Exception e) {
      log.error("[DLQ BATCH] 배치 복구 중 오류 발생: {}", e.getMessage());
    }

    log.info("[DLQ BATCH] reservation.dlq.failed 메시지 복구 완료!");
  }
}
