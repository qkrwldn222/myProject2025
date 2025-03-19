package com.reservation.common.config;

import com.reservation.domain.ReservationEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ProducerFactory<String, ReservationEvent> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // JSON 직렬화 설정
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, ReservationEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic reservationDlqTopic() {
    return TopicBuilder.name("reservation.dlq")
            .partitions(3) // DLQ는 병렬 처리를 위해 3개의 파티션 사용
            .replicas(1)   // 복제본 개수 (1개 설정)
            .build();
  }

  @Bean
  public NewTopic reservationDlqFailedTopic() {
    return TopicBuilder.name("reservation.dlq.failed")
            .partitions(1) //최종 실패 메시지는 단일 파티션으로 설정
            .replicas(1)
            .build();
  }

  @Bean
  public NewTopic reservationCreatedTopic() {
    return new NewTopic("reservation.created", 3, (short) 1);
  }

  @Bean
  public NewTopic reservationCancelledTopic() {
    return new NewTopic("reservation.cancelled", 3, (short) 1);
  }

  @Bean
  public NewTopic reservationReviewedTopic() {
    return new NewTopic("reservation.reviewed", 3, (short) 1);
  }

  @Bean
  public NewTopic reservationCompletedTopic() {
    return new NewTopic("reservation.completed", 3, (short) 1);
  }
}
