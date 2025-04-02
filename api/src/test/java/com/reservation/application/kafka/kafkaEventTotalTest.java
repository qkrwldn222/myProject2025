package com.reservation.application.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import com.reservation.application.usecase.ReservationEventUseCase;
import com.reservation.common.enums.*;
import com.reservation.domain.*;
import com.reservation.infrastructure.kafka.ReservationDLQBatchProcessor;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = {"reservation.created", "reservation.dlq", "reservation.dlq.failed"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class kafkaEventTotalTest {

  @Autowired private KafkaTemplate<String, ReservationEvent> kafkaTemplate;

  private KafkaTemplate<String, ReservationEvent> mockKafkaTemplate;

  @Autowired private ReservationDLQBatchProcessor reservationDLQBatchProcessor;

  @Autowired private ReservationEventUseCase originalReservationEventUseCase;

  private ReservationEventUseCase reservationEventUseCase;

  @BeforeEach
  void setUp() {
    reservationEventUseCase = Mockito.spy(originalReservationEventUseCase);
    mockKafkaTemplate = Mockito.mock(KafkaTemplate.class);
  }

  @Test
  void testKafkaSentSuccessfully() {
    // Given
    ReservationEvent event =
        new ReservationEvent(
            ReservationEventStatus.CREATED,
            1L,
            1L,
            1L,
            ReservationStatus.PENDING,
            LocalDateTime.now());
    // When
    CompletableFuture<org.springframework.kafka.support.SendResult<String, ReservationEvent>> send =
        kafkaTemplate.send("reservation.created", event);

    // Then
    assertThat(send).isNotNull();
  }

  @Test
  void testDLQMessageHandling() throws InterruptedException {
    // Given
    ReservationEvent event =
        new ReservationEvent(
            ReservationEventStatus.CREATED,
            1L,
            1L,
            1L,
            ReservationStatus.PENDING,
            LocalDateTime.now());

    // DLQ로 메시지 전송 (재시도 횟수 2)
    kafkaTemplate.send(
        MessageBuilder.withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, "reservation.dlq")
            .setHeader("retry-count", 2)
            .build());

    // DLQ 메시지가 처리될 시간을 확보
    Thread.sleep(5000);

    // DLQ 메시지가 Consumer에 의해 처리되었는지 검증
    await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> verify(reservationEventUseCase, atLeastOnce()).handleReservationEvent(event));
  }

  @Test
  void testKafkaMessageConsumedSuccessfully() throws InterruptedException {
    // Given
    ReservationEvent event =
        new ReservationEvent(
            ReservationEventStatus.CREATED,
            1L,
            1L,
            1L,
            ReservationStatus.PENDING,
            LocalDateTime.now());

    // Kafka 메시지 전송
    kafkaTemplate.send("reservation.created", event);

    // 메시지가 Consumer에서 처리될 충분한 시간 확보
    Thread.sleep(5000);

    // kafka Consumer가 정상적으로 메시지를 처리했는지 검증
    await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> verify(reservationEventUseCase, atLeastOnce()).handleReservationEvent(event));
  }
}
