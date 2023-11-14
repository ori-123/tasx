package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.service.user.MockUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/users")
public class MockUserController {
  private final MockUserProvider provider;

  @Autowired
  public MockUserController(MockUserProvider provider) {
    this.provider = provider;
  }

  @GetMapping()
  public ResponseEntity<?> getAllUsers() {
    List<UserResponsePublicDto> users = provider.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("data", users));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserById(@PathVariable Long userId) {
    Optional<UserResponsePublicDto> user = provider.getById(userId);
    if (user.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", user));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        Map.of("error", HttpStatus.NOT_FOUND.getReasonPhrase()));
    }
  }

  @GetMapping("/populate")
  public ResponseEntity<?> populateUsers() {
    provider.populate();
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Users created successfully"));
  }
}
