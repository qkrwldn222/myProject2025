package com.reservation.adapter.security.swagger;

import com.reservation.adapter.security.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "권한 관련", description = "권한관련 API")
public interface AuthSwagger {

  @Operation(summary = "사용자 등록", description = "사용자 등록 API를 호충합니다..")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "유저 등록이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PostMapping("/signup")
  ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest);

  @Operation(summary = "사용자 등록", description = "사용자 등록 API를 호충합니다..")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "유저 등록이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @Parameters({@Parameter(name = "code", description = "인증코드", required = true)})
  @PostMapping("/kakao/signup")
  ResponseEntity<?> registerUser(@RequestParam("code") String code);

  @Operation(summary = "사용자 로그인", description = "사용자 인증 후 JWT 토큰을 발급합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "JWT 토큰 발급 완료"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/login")
  ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest);

  @Operation(summary = "사용자 로그아웃", description = "로그아웃하여 JWT 토큰을 무효화합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/logout")
  ResponseEntity<String> logout(@RequestHeader("Authorization") String token);

  @Operation(summary = "토큰 재발급", description = "JWT 토큰을 재발급 합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "재발급 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/refresh-token")
  ResponseEntity<JwtResponse> refreshToken(@RequestHeader("Authorization") String token);

  @Operation(summary = "회원 탈퇴", description = "사용자를 삭제합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @DeleteMapping("/delete")
  ResponseEntity<String> deleteUser(
      @RequestHeader("Authorization") String token,
      @RequestBody DeleteUserRequest deleteUserRequest);

  @Operation(summary = "비밀번호 찾기 - 인증번호 요청", description = "이메일로 인증번호를 전송합니다.")
  @PostMapping("/verify-request")
  ResponseEntity<String> requestPasswordVerification(
      @Parameter(description = "이메일", required = true) @RequestParam(name = "email") String email);

  @Operation(
      summary = "비밀번호 찾기 - 인증번호 검증",
      description = "입력한 인증번호가 올바른지 확인하고, 인증되면 비밀번호 변경 토큰을 발급합니다.")
  @PostMapping("/verify")
  ResponseEntity<String> verifyCode(@RequestBody PasswordVerifyRequest request);

  @Operation(summary = "비밀번호 변경", description = "발급받은 토큰을 사용하여 비밀번호를 변경합니다.")
  @PostMapping("/reset")
  ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request);
}
