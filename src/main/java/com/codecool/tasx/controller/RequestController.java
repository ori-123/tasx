package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestUpdateDto;
import com.codecool.tasx.service.request.RequestService;
import com.codecool.tasx.service.security.AuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/requests")
public class RequestController {
  private final RequestService requestService;
  private final AuthProvider authProvider;
  private final Logger logger;

  @Autowired
  public RequestController(RequestService requestService, AuthProvider authProvider) {
    this.requestService = requestService;
    this.authProvider = authProvider;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping()
  public ResponseEntity<?> readJoinRequestsOfCompany(
    @PathVariable Long companyId,
    HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    List<CompanyJoinRequestResponseDto> requests =
      requestService.getJoinRequestsOfCompany(companyId, userId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "data", requests));
  }

  @PostMapping()
  public ResponseEntity<?> joinCompany(@PathVariable Long companyId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    CompanyJoinRequestResponseDto createdRequest = requestService.createJoinRequest(
      userId,
      companyId);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "message",
      "Request created successfully",
      "data", createdRequest));
  }

  @PutMapping("/{requestId}")
  public ResponseEntity<?> updateJoinRequestById(
    @PathVariable Long requestId,
    @RequestBody CompanyJoinRequestUpdateDto requestDto, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    requestService.handleJoinRequest(userId, requestId, requestDto);

    //TODO: notify the user who requested to join...
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message", "Request updated successfully"));
  }
}