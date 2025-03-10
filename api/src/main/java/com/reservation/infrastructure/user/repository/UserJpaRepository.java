package com.reservation.infrastructure.user.repository;

import com.reservation.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByUserID(String userID);

  Optional<User> findByEmail(String email);

  boolean existsByUserID(String userID);

  boolean existsByUsername(String username);

  List<User> findAll();
}
