package com.reservation.application.user.service;

import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.repository.UserKakaoRepository;
import com.reservation.common.service.EmailService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.RoleType;
import com.reservation.domain.KakaoUserInfo;
import com.reservation.domain.Role;
import com.reservation.domain.User;
import com.reservation.infrastructure.role.repository.RoleJpaRepository;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserRestService implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserRestService.class);

  private final RedisTemplate<String, Object> redisTemplate;
  private final UserJpaRepository userRepository;
  private final RoleJpaRepository roleRepository;
  private final UserKakaoRepository userKakaoRepository;
  private final EmailService emailService; // 이메일 전송 서비스

  private static final long AUTH_CODE_EXPIRATION = 5 * 60; // 5분

  public List<User> getAllUsers() {
    UserService proxyInstance = (UserService) AopContext.currentProxy(); // Spring 프록시 활용
    return proxyInstance.findAll();
  }

  @Cacheable(value = "users", sync = true)
  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  // 캐시 초기화 (무효화)
  @CacheEvict(value = "users", allEntries = true)
  public void clearUsersCache() {
    logger.info("유저 정보 캐시를 초기화했습니다.");
  }

  @Override
  public Optional<User> findByUserId(String userId) {
    return getAllUsers().stream().filter(data -> data.getUserID().equals(userId)).findFirst();
  }

  @Override
  public Optional<User> findById(Long userId) {
    return getAllUsers().stream().filter(data -> data.getId().equals(userId)).findFirst();
  }

  @Transactional
  public void registerUser(SignupCommand command) {
    command.validate();

    if (userRepository.existsByUserID(command.getUserID())) {
      throw new ApiException("아이디가 이미 존재합니다.");
    }

    RoleType roleType = RoleType.fromCode(command.getRoleCode());
    Role role =
        roleRepository.findByRoleType(roleType).orElseThrow(() -> new ApiException("권한이 없습니다."));

    User userEntity =
        User.builder()
            .username(command.getUserName())
            .userID(command.getUserID())
            .password(command.getPassword())
            .role(role)
            .build();

    userRepository.save(userEntity);
  }

  @Transactional
  public void deleteUserByUserID(String userID) {
    Optional<User> user = userRepository.findByUserID(userID);
    user.ifPresent(userRepository::delete);
  }

  public Optional<User> findByUsername(String username) {
    return this.getAllUsers().stream()
        .filter(data -> data.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  @Transactional
  public User findOrCreateKakaoUser(String code) {
    String kakaoAccessToken = userKakaoRepository.getKakaoAccessToken(code);

    KakaoUserInfo kakaoUserInfo = userKakaoRepository.getKakaoUserInfo(kakaoAccessToken);

    Optional<User> kakaoUser = userRepository.findByUserID(String.valueOf(kakaoUserInfo.getId()));
    if (kakaoUser.isPresent()) {
      return kakaoUser.get();
    } else {
      Role defaultRole =
          roleRepository
              .findByRoleType(RoleType.USER) // 기본 역할(사용자)
              .orElseThrow(() -> new RuntimeException("기본 역할을 찾을 수 없습니다."));
      return kakaoUser.orElseGet(
          () -> {
            User newUser =
                User.builder()
                    .username(kakaoUserInfo.getNickname())
                    .userID(String.valueOf(kakaoUserInfo.getId()))
                    .provider("kakao")
                    .role(defaultRole)
                    .build();
            return userRepository.save(newUser);
          });
    }
  }

  @Override
  public void requestVerification(String email) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new ApiException("등록된 이메일이 없습니다."));
    String authCode = String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 랜덤 코드

    // Redis에 저장 (5분 유효)
    redisTemplate
        .opsForValue()
        .set("AUTH_CODE:" + email, authCode, AUTH_CODE_EXPIRATION, TimeUnit.SECONDS);

    // 인증번호 이메일 발송
    emailService.sendEmail(email, "비밀번호 찾기 인증번호", "인증번호: " + authCode);
  }

  @Override
  public String verifyCode(String email, String verificationCode) {
    String storedCode = redisTemplate.opsForValue().get("AUTH_CODE:" + email).toString();
    if (!StringUtils.hasText(storedCode) || !storedCode.equals(verificationCode)) {
      throw new ApiException("잘못된 인증번호이거나 만료되었습니다.");
    }

    // 인증 성공 시, 비밀번호 변경 토큰 발급 (10분 유효)
    String resetToken = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set("RESET_TOKEN:" + resetToken, email, Duration.ofMinutes(10));

    // 기존 인증번호 삭제
    redisTemplate.delete("VERIFICATION_CODE:" + email);

    return resetToken;
  }

  @Override
  @Transactional
  public User resetPassword(String resetToken, String newPassword) {
    String email = (String) redisTemplate.opsForValue().get("RESET_TOKEN:" + resetToken);

    if (email == null) {
      throw new ApiException("유효하지 않은 토큰이거나 만료되었습니다.");
    }

    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

    //    user.updatePassword(newPassword);
    // 비밀번호 암호화 후 저장
    userRepository.save(user);

    // 토큰 삭제 (재사용 방지)
    redisTemplate.delete("RESET_TOKEN:" + resetToken);
    return user;
  }
}
