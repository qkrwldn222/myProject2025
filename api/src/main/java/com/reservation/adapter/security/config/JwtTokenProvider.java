package com.reservation.adapter.security.config;

import com.reservation.common.config.ApiException;
import com.reservation.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${security.jwt.secret}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private long validityInMilliseconds;

  private Key secret;

  @PostConstruct
  protected void init() {
    this.secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  /** JWT 토큰 생성 (Role 포함) */
  public String createToken(String username, String roleCode) {
    Set<String> existingTokens = redisTemplate.keys("*");
    // 1. 기존 토큰 확인 (사용자가 이미 로그인한 경우 기존 토큰 가져오기)
    String existingToken = null;
    for (String token : existingTokens) {
      if (username.equals(getUsernameFromToken(token))) {
        existingToken = token;
        break;
      }
    }
    // 2. 기존 토큰이 있으면 새 토큰으로 갱신
    if (existingToken != null) {
      return refreshJwtToken(existingToken);
    }

    // 3. 기존 토큰이 없으면 새 토큰 생성
    return generateNewToken(username, roleCode);
  }

  private String generateNewToken(String username, String roleCode) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("role", roleCode);

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

    String jwt =
        Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();

    // Redis에 저장 (만료시간 설정)
    redisTemplate.opsForValue().set(jwt, roleCode, validityInMilliseconds, TimeUnit.MILLISECONDS);

    return jwt;
  }

  /** Spring Security `Authentication` 객체로부터 JWT 생성 */
  public String createToken(Authentication authentication, User user) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return createToken(
        userDetails.getUsername(), user.getRole().getRoleType().getCode()); // 기본 ROLE: USER
  }

  /** JWT에서 Role 정보 추출 */
  public String getUserRoleFromToken(String token) {
    try {
      Claims claims =
          Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();

      return claims.get("role", String.class);
    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }

  /** JWT 토큰 검증 (유효성 체크) */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
      for (String aa : redisTemplate.keys("*")) {
        System.out.println(aa);
      }
      // Redis에 저장된 토큰인지 확인 (없으면 만료된 것)
      return redisTemplate.hasKey(token);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /** JWT에서 사용자명(Username) 추출 */
  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /** JWT 토큰 추가 (로그아웃) */
  public void revokeToken(String token) {
    if (!token.startsWith("Bearer ")) {
      throw new ApiException("유효하지 않은 JWT 형식입니다.");
    }
    redisTemplate.delete(token.substring(7));
  }

  public String refreshJwtToken(String oldJwt) {
    if (validateToken("Bearer " + oldJwt)) {
      throw new ApiException(" 만료되었거나 존재하지 않는 토큰입니다.");
    }

    String roleCode = (String) redisTemplate.opsForValue().get(oldJwt);

    // 기존 토큰 삭제
    redisTemplate.delete(oldJwt);

    // 새 토큰 생성
    String newJwt = createToken(getUsernameFromToken(oldJwt), roleCode);

    // Redis에 저장 (새로운 만료 시간 적용)
    redisTemplate
        .opsForValue()
        .set(newJwt, roleCode, validityInMilliseconds, TimeUnit.MILLISECONDS);

    return newJwt;
  }

  public void revokeTokenByUser(String username) {
    Set<String> keys = redisTemplate.keys("*"); // Redis에 저장된 모든 키 가져오기
    if (keys != null) {
      for (String token : keys) {
        try {
          String storedUsername = getUsernameFromToken(token);
          if (username.equals(storedUsername)) {
            redisTemplate.delete(token); // 해당 사용자 토큰 삭제
          }
        } catch (Exception ignored) {
          // 유효하지 않은 토큰이면 무시
        }
      }
    }
  }

  public void clearAllJwtTokens() {
    Set<String> keys = redisTemplate.keys("*");
    if (keys != null) {
      for (String token : keys) {
        try {
          // 토큰인지 확인 후 삭제
          if (getUsernameFromToken(token) != null) {
            redisTemplate.delete(token);
          }
        } catch (Exception ignored) {
          // 유효하지 않은 토큰이면 무시
        }
      }
    }
  }

  public void clearAllReservationLocks() {
    Set<String> keys = redisTemplate.keys("reservation:*"); // 예약 관련된 키만 삭제
    keys.addAll(redisTemplate.keys("user-reservation:*"));
    if (keys != null) {
      redisTemplate.delete(keys);
    }
  }
}
