package com.reservation.application.user.service;



import com.reservation.domain.User;
import com.reservation.application.user.model.SignupCommand;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {
    @Transactional
    public void registerUser(SignupCommand command);
    public Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);
}
