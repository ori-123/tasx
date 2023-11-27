package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {
  private final RequestService requestService;

  @Autowired
  public UserController(RequestService requestService) {
    this.requestService = requestService;
  }

  @GetMapping("/requests")
  public ResponseEntity<?> getJoinRequestsOfUser() {
    List<CompanyJoinRequestResponseDto> joinRequests = requestService.getJoinRequestsOfUser();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "data", joinRequests
    ));
  }
}
