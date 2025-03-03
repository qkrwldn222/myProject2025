package com.reservation.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CoreConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    // 로컬 Redis 서버에 연결 (포트 6379)
    return new LettuceConnectionFactory("localhost", 6379);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer()); // 직렬화 설정
    return template;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
