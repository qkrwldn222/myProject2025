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
}
