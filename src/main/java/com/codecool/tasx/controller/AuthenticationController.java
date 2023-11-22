package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.user.auth.*;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(
    @RequestBody RegisterRequestDto request) {
    authenticationService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "message", "Account created successfully"));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(
    @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
    LoginResponseDto loginResponse = authenticationService.login(loginRequest);

    String refreshToken = authenticationService.getNewRefreshToken(
      loginResponse.userInfo().email());
    addRefreshCookie(refreshToken, response);
    return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
  }

  @GetMapping("/refresh")
  public ResponseEntity<RefreshResponseDto> refresh(@CookieValue(required = false) String jwt) {
    if (jwt == null) {
      throw new UnauthorizedException("Refresh token cookie not found");
    }
    RefreshResponseDto refreshResponse = authenticationService.refresh(new RefreshRequestDto(jwt));
    return ResponseEntity.status(HttpStatus.OK).body(refreshResponse);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(
    @CookieValue(required = false) String jwt,
    HttpServletResponse response) {
    if (jwt == null) {
      return ResponseEntity.noContent().build();
    }
    clearRefreshCookie(response);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message", "Account logged out successfully"));
  }

  private void addRefreshCookie(String refreshToken, HttpServletResponse response) {
    response.setHeader(
      "Set-Cookie", "jwt=" + refreshToken + "; HttpOnly; Secure; SameSite=Strict; Path=/");
  }

  private void clearRefreshCookie(HttpServletResponse response) {
    response.setHeader("Set-Cookie", "jwt=; HttpOnly; Secure; SameSite=Strict; Path=/");
  }
}
