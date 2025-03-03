package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupRequest {
  @Schema(description = "userID", example = "qkrwldn222")
  private String userID;

  @Schema(description = "이름", example = "박지우")
  private String userName;

  @Schema(description = "비밀번호", example = "1234")
  private String password;

  @Schema(description = "권한코드", example = "01")
  private String roleCode;
}
