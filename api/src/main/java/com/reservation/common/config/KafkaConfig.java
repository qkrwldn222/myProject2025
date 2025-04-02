package com.reservation.common.config;

import com.reservation.domain.ReservationEvent;
import com.reservation.infrastructure.kafka.model.ReviewMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ProducerFactory<String, ReservationEvent> producerFactory() {
    System.out.println(bootstrapServers);
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
  public ProducerFactory<String, ReviewMessage> reviewProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, ReviewMessage> reviewKafkaTemplate() {
    return new KafkaTemplate<>(reviewProducerFactory());
  }

  @Bean
  public KafkaAdmin.NewTopics topics() {
    return new KafkaAdmin.NewTopics(
        new NewTopic("reservation.created", 3, (short) 1),
        new NewTopic("reservation.cancelled", 3, (short) 1),
        new NewTopic("reservation.reviewed", 3, (short) 1),
        new NewTopic("reservation.completed", 3, (short) 1),
        new NewTopic("reservation.dlq", 3, (short) 1),
        new NewTopic("reservation.dlq.failed", 1, (short) 1));
  }
}
