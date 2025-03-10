package com.reservation.application.user.service;

import com.reservation.application.user.model.SignupCommand;
import com.reservation.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

  void registerUser(SignupCommand command);

  Optional<User> findByUsername(String username);

  Optional<User> findByUserId(String userId);

  boolean existsByUsername(String username);

  void deleteUserByUserID(String userID);

  List<User> findAll();

  /**
   * 카카이 인증코드 -> 유저 인증 or 회원가입
   *
   * @param code 카카오 인증코드
   * @return User 도메인
   */
  User findOrCreateKakaoUser(String code);

  /**
   * 메일로 인증번호 전송
   *
   * @param email 이메일
   */
  void requestVerification(String email);

  /**
   * 인증번호 검증 및 비밀번호 변경 토큰 발급
   *
   * @param email 이메일
   * @param verificationCode 인증코드
   * @return String 비밀번호 변경토큰
   */
  String verifyCode(String email, String verificationCode);

  /**
   * 비밀번호 변경
   *
   * @param resetToken 비밀번호 변경토큰
   * @param newPassword 신규 비밀번호
   */
  User resetPassword(String resetToken, String newPassword);
}
