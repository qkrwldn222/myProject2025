package com.reservation.application.user.service;

import com.reservation.application.user.model.SignupCommand;
import com.reservation.common.config.ApiException;
import com.reservation.common.config.RoleType;
import com.reservation.domain.Role;
import com.reservation.domain.User;
import com.reservation.infrastructure.role.repository.RoleJpaRepository;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRestService implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserRestService.class);

  private final UserJpaRepository userRepository;
  private final RoleJpaRepository roleRepository;

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
}
