package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordVerifyRequest {

  @Schema(description = "이메일", example = "user@example.com")
  @NotBlank
  private String email;

  @Schema(description = "전화번호", example = "010-1234-5478, 01012344567")
  private String phone;

  @Schema(description = "인증번호", example = "123456")
  @NotBlank
  private String verificationCode;
}
