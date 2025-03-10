package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {

  @NotBlank
  @Schema(description = "비밀번호 변경 토큰", example = "UUID")
  private String resetToken;

  @NotBlank
  @Schema(description = "새 비밀번호", example = "newpassword123!")
  private String newPassword;
}
