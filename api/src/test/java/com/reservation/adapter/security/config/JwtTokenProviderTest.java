package com.reservation.adapter.security.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

  @Mock private RedisTemplate<String, Object> redisTemplate;
  @Mock private ValueOperations<String, Object> valueOperations;

  private JwtTokenProvider jwtTokenProvider;

  @BeforeEach
  void setUp() {
    jwtTokenProvider = new JwtTokenProvider(redisTemplate);
    ReflectionTestUtils.setField(
        jwtTokenProvider,
        "secretKey",
        "mySecretKeyForJwtTokenExample2025mySecretKeyForJwtTokenExample2025");
    ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    jwtTokenProvider.init();

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.keys("jwt:*")).thenReturn(Collections.emptySet());
  }

  @Test
  void revokeTokenDeletesPrefixedRedisKey() {
    String token = jwtTokenProvider.createToken("user1", "1");

    jwtTokenProvider.revokeToken("Bearer " + token);

    verify(redisTemplate).delete("jwt:" + token);
  }

  @Test
  void validateTokenReturnsTrueWhenRedisKeyExists() {
    String token = jwtTokenProvider.createToken("user1", "1");
    when(redisTemplate.hasKey("jwt:" + token)).thenReturn(true);

    boolean valid = jwtTokenProvider.validateToken(token);

    assertTrue(valid);
  }

  @Test
  void refreshTokenReturnsNewTokenAndDeletesOldKey() {
    String oldToken = jwtTokenProvider.createToken("user1", "1");
    when(redisTemplate.hasKey("jwt:" + oldToken)).thenReturn(true);
    when(valueOperations.get("jwt:" + oldToken)).thenReturn("1");

    String newToken = jwtTokenProvider.refreshJwtToken(oldToken);

    assertNotNull(newToken);
    assertNotEquals(oldToken, newToken);
    verify(redisTemplate).delete("jwt:" + oldToken);
    verify(valueOperations, atLeast(2))
        .set(anyString(), any(), anyLong(), any(java.util.concurrent.TimeUnit.class));
  }
}
