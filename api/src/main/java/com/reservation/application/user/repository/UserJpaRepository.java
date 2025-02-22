package com.reservation.application.user.repository;

import com.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        boolean existsByUsername(String username);
}