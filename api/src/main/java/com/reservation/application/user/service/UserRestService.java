package com.reservation.application.user.service;

import com.reservation.common.config.ApiException;
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

        if (userRepository.existsByUserID(command.getUserID())) {
            throw new ApiException("아이디가 이미 존재합니다.");
        }

        Role role = roleRepository.findByCode(command.getRoleCode())
                .orElseThrow(() -> new ApiException("권한이 없습니다."));

        User userEntity = User.builder()
                .username(command.getUserName())
                .userID(command.getUserID())
                .password(command.getPassword())
                .role(role)
                .build();

        userRepository.save(userEntity);
    }

    public Role findRoleByCode(String roleCode) {
        return roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new ApiException("Role not found!"));
    }

    @Transactional
    public void deleteUserByUserID(String userID) {
        Optional<User> user = userRepository.findByUserID(userID);
        user.ifPresent(userRepository::delete);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
