package com.reservation.application.user.service;



import com.reservation.domain.User;
import com.reservation.application.user.model.SignupCommand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    @Transactional
    void registerUser(SignupCommand command);
    Optional<User> findByUsername(String username);

    Optional<User> findByUserId(String userId);

    boolean existsByUsername(String username);

    void deleteUserByUserID(String userID);

    List<User> findAll();
}
