package com.reservation.adapter.security.controller;


import com.reservation.adapter.security.config.JwtTokenProvider;
import com.reservation.adapter.security.mapper.SecurityRequestMapper;
import com.reservation.adapter.security.model.JwtResponse;
import com.reservation.adapter.security.model.LoginRequest;
import com.reservation.adapter.security.model.SignupRequest;
import com.reservation.adapter.security.swagger.AuthSwagger;
import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.service.UserService;
import com.reservation.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        SignupCommand command = SecurityRequestMapper.INSTANCE.toCommand(signupRequest);
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        command.setPassword(encodedPassword);  // 인코딩된 비밀번호 설정
        userService.registerUser(command);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // 인증 성공 시 JWT 토큰 생성
        String jwt = jwtProvider.createToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }


    @PostMapping("/temp/token")
    public ResponseEntity<JwtResponse> generateTempAdminToken() {
        User admin = userService.findByUsername("admin")
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        String jwt = jwtProvider.createToken(admin.getUsername(), admin.getRole().getCode());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
