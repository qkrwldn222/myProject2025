package com.reservation.application.user.service;



import com.reservation.domain.User;
import com.reservation.application.user.model.SignupCommand;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {
    @Transactional
    void registerUser(SignupCommand command);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteUserByUserID(String userID);

}
