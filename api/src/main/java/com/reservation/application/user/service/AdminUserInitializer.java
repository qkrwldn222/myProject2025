package com.reservation.application.user.service;

import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.repository.RoleJpaRepository;
import com.reservation.common.config.UserRole;
import com.reservation.domain.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserService userService;
    private final RoleJpaRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initAdminUser() {
        // Role이 없으면 생성
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setCode("00");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setCode("01");
            roleRepository.save(userRole);

            Role storeOwnerRole = new Role();
            storeOwnerRole.setName("OWNER");
            storeOwnerRole.setCode("02");
            roleRepository.save(storeOwnerRole);
        }

        // Admin 계정이 없으면 생성
        if (!userService.existsByUsername("admin")) {
            SignupCommand command = new SignupCommand();
            command.setUserID("admin");
            command.setUserName("admin");
            command.setPassword(passwordEncoder.encode("1234")); // 암호화된 비밀번호
            command.setRoleCode(UserRole.ADMIN.getCode());
            userService.registerUser(command);
        }
    }
}