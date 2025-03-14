package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
  @Schema(description = "유저 이름", example = "jiwoo9495")
  @NotBlank
  private String userId;

  @Schema(description = "비밀번호", example = "1234")
  @NotBlank
  private String password;
}
