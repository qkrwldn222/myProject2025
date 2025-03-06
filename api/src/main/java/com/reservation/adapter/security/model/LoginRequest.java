package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginRequest {
  @Schema(description = "유저 이름", example = "박지우")
  private String username;

  @Schema(description = "비밀번호", example = "1234")
  private String password;
}
