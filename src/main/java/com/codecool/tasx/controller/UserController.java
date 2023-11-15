package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.service.company.CompanyService;
import com.codecool.tasx.service.security.AuthProvider;
import jakarta.servlet.http.HttpServletRequest;
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
  private final AuthProvider authProvider;
  private final CompanyService companyService;

  @Autowired
  public UserController(AuthProvider authProvider, CompanyService companyService) {
    this.authProvider = authProvider;
    this.companyService = companyService;
  }

  @GetMapping("/requests")
  public ResponseEntity<?> getJoinRequestsOfUser(HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    List<CompanyJoinRequestResponseDto> joinRequests = companyService.getJoinRequestsOfUser(userId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "data", joinRequests
    ));
  }
}
