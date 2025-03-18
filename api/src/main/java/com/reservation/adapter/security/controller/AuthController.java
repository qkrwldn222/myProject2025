package com.reservation.adapter.security.controller;

import com.reservation.adapter.security.config.JwtTokenProvider;
import com.reservation.adapter.security.mapper.SecurityRequestMapper;
import com.reservation.adapter.security.model.*;
import com.reservation.adapter.security.swagger.AuthSwagger;
import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
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
    command.setPassword(encodedPassword); // 인코딩된 비밀번호 설정
    userService.registerUser(command);

    return ResponseEntity.ok("가입이 완료되었습니다.");
  }

  /**
   * https://kauth.kakao.com/oauth/authorize?client_id=YOUR_KAKAO_CLIENT_ID&redirect_uri=YOUR_REDIRECT_URI&response_type=code
   *
   * @param code 카카오 인증코드
   * @return message : 요청이 완료되었습니다.
   */
  @Override
  @PostMapping("/kakao/signup")
  public ResponseEntity<?> registerUser(@RequestParam("code") String code) {
    User user = userService.findOrCreateKakaoUser(code);

    String jwt =
        jwtProvider.createToken(user.getUsername(), user.getRole().getRoleType().getCode());

    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
    // 1. 사용자 조회
    User user =
        userService
            .findByUserId(loginRequest.getUserId())
            .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

    // 2. 비밀번호 검증
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body(new JwtResponse("인증 실패: 비밀번호가 일치하지 않습니다."));
    }

    // 3. 인증 객체 생성 및 인증
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(), loginRequest.getPassword()));
    // 4. JWT 토큰 생성
    String jwt = jwtProvider.createToken(authentication, user);

    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  @PostMapping("/temp/token")
  public ResponseEntity<JwtResponse> generateTempAdminToken() {
    User admin =
        userService
            .findByUsername("admin")
            .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

    String jwt =
        jwtProvider.createToken(admin.getUsername(), admin.getRole().getRoleType().getCode());
    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    jwtProvider.revokeToken(token);
    return ResponseEntity.ok("로그아웃 되었습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(
      @RequestHeader("Authorization") String token, @RequestBody DeleteUserRequest request) {
    // 토큰에서 유저 정보 가져오기
    String username = jwtProvider.getUsernameFromToken(token);

    User user =
        userService.findByUsername(username).orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

    // 비밀번호 검증
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())
        || !request.getUserID().equals(user.getUserID())) {
      throw new ApiException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
    // 유저 삭제
    userService.deleteUserByUserID(request.getUserID());

    // 토큰 무효화
    jwtProvider.revokeToken(token);
    return ResponseEntity.ok("탈퇴가 안료되었습니다.");
  }

  /** JWT 토큰 재발급 API */
  @PostMapping("/refresh-token")
  public ResponseEntity<JwtResponse> refreshToken(@RequestHeader("Authorization") String token) {
    String oldJwt = token.substring(7);
    String newJwt = jwtProvider.refreshJwtToken(oldJwt);
    return ResponseEntity.ok(new JwtResponse(newJwt));
  }

  @Override
  @PostMapping("/password/verify-request")
  public ResponseEntity<String> requestPasswordVerification(@RequestParam("email") String email) {
    userService.requestVerification(email);
    return ResponseEntity.ok("인증번호가 전송되었습니다.");
  }

  @Override
  @PostMapping("/password/verify")
  public ResponseEntity<String> verifyCode(@RequestBody PasswordVerifyRequest request) {

    String resetToken = userService.verifyCode(request.getEmail(), request.getVerificationCode());

    return ResponseEntity.ok(resetToken);
  }

  @Override
  @PostMapping("/password/reset")
  public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {

    String encodedPassword = passwordEncoder.encode(request.getNewPassword());

    User user = userService.resetPassword(request.getResetToken(), encodedPassword);

    jwtProvider.revokeTokenByUser(String.valueOf(user.getUserID()));

    return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
  }
}
