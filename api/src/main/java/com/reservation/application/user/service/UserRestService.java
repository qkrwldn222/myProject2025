package com.reservation.application.user.service;

import com.reservation.domain.Role;
import com.reservation.domain.User;
import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.repository.RoleJpaRepository;
import com.reservation.application.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRestService implements UserService {
    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;

    @Transactional
    public void registerUser(SignupCommand command) {
        command.validate();

        if (userRepository.existsByUsername(command.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        Role role = roleRepository.findById(command.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        User userEntity = User.builder()
                .username(command.getUsername())
                .name(command.getName())
                .password(command.getPassword()) // 이미 암호화된 상태로 전달
                .role(role) // Role 객체 직접 주입
                .build();

        userRepository.save(userEntity);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
