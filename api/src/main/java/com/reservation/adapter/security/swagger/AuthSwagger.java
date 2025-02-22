package com.reservation.adapter.security.swagger;


import com.reservation.adapter.security.model.JwtResponse;
import com.reservation.adapter.security.model.LoginRequest;
import com.reservation.adapter.security.model.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "권한 관련", description = "권한관련 API")
public interface AuthSwagger {

    @Operation(summary = "사용자 등록", description = "사용자 등록 API를 호충합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 등록이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/signup")
    ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest);

    @Operation(summary = "사용자 로그인", description = "사용자 JWT token을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 인증이 완료되었습니다."),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest);
}
