package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.service.populate.MockDataProvider;
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
@RequestMapping("/api/v1/populate")
public class PopulateDataController {
  private final MockDataProvider provider;

  @Autowired
  public PopulateDataController(MockDataProvider provider) {
    this.provider = provider;
  }

  @GetMapping()
  public ResponseEntity<?> populateUsers() {
    provider.populate();
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Mock data created successfully"));
  }
}
