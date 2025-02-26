package com.reservation.adapter.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteUserRequest {

    @NotNull
    @Schema(description = "유저 ID", example = "qkrwldn222")
    private String userID;
    @NotNull
    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
