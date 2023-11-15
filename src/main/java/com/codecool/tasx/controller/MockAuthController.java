package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.user.UserCreateRequestDto;
import com.codecool.tasx.controller.dto.user.UserLoginRequestDto;
import com.codecool.tasx.controller.dto.user.auth.MockAuthenticationDto;
import com.codecool.tasx.controller.dto.user.auth.MockUserInfoDto;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class MockAuthController {
  private final UserDao userDao;

  @Autowired
  public MockAuthController(UserDao userDao) {
    this.userDao = userDao;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UserCreateRequestDto createRequestDto) {
    try {
      userDao.save(new User(createRequestDto.username(), createRequestDto.email(),
        createRequestDto.password()));
      return ResponseEntity.status(HttpStatus.CREATED).body(
        Map.of("message", "Account created successfully"));
    } catch (ConstraintViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
        Map.of("error", "Username or e-mail already taken"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("error", "Failed to create account"));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginRequestDto loginRequestDto) {
    try {
      Optional<User> foundUser = userDao.findOneByEmail(loginRequestDto.email());

      if (foundUser.isPresent()) {
        MockAuthenticationDto authenticationDto = new MockAuthenticationDto(
          foundUser.get().getId().toString(),
          new MockUserInfoDto(foundUser.get().getUsername(), foundUser.get().getEmail(),
            foundUser.get().getRoles()));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", authenticationDto));
      }
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Wrong e-mail address or password"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Failed to log in"));
    }
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh() {
    try {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Not implemented in mock authentication"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Failed to refresh"));
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(
        Map.of("message", "Account logged out successfully"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Failed to log out"));
    }
  }
}
