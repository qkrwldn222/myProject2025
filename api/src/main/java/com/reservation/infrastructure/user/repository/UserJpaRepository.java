package com.reservation.infrastructure.user.repository;

import com.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        Optional<User> findByUserID(String username);
        boolean existsByUserID(String userID);
        boolean existsByUsername(String username);
        List<User> findAll();
}