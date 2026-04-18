package com.reservation.adapter.security.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reservation.adapter.security.config.JwtTokenProvider;
import com.reservation.adapter.security.model.DeleteUserRequest;
import com.reservation.adapter.security.model.JwtResponse;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private UserService userService;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtTokenProvider jwtProvider;
  @Mock private PasswordEncoder passwordEncoder;

  private AuthController authController;

  @BeforeEach
  void setUp() {
    authController =
        new AuthController(userService, authenticationManager, jwtProvider, passwordEncoder);
  }

  @Test
  void refreshTokenThrowsWhenHeaderIsInvalid() {
    assertThrows(ApiException.class, () -> authController.refreshToken("invalid-token"));
  }

  @Test
  void refreshTokenUsesRawJwtValue() {
    when(jwtProvider.refreshJwtToken("old-token")).thenReturn("new-token");

    JwtResponse response = authController.refreshToken("Bearer old-token").getBody();

    assertNotNull(response);
    assertEquals("new-token", response.getToken());
    verify(jwtProvider).refreshJwtToken("old-token");
  }

  @Test
  void deleteUserParsesBearerTokenBeforeProviderCall() {
    DeleteUserRequest request = new DeleteUserRequest();
    request.setUserID("user-1");
    request.setPassword("pw");

    User user = User.builder().userID("user-1").username("tester").password("encodedPw").build();

    when(jwtProvider.getUsernameFromToken("raw-token")).thenReturn("tester");
    when(userService.findByUsername("tester")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("pw", "encodedPw")).thenReturn(true);

    authController.deleteUser("Bearer raw-token", request);

    verify(jwtProvider).getUsernameFromToken("raw-token");
    verify(jwtProvider).revokeToken("raw-token");
  }
}
