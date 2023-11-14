package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestUpdateDto;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.service.company.CompanyService;
import com.codecool.tasx.service.populate.MockDataProvider;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final MockDataProvider mockDataProvider;
  private final Logger logger;

  @Autowired
  public CompanyController(CompanyService companyService, MockDataProvider mockDataProvider) {
    this.companyService = companyService;
    this.mockDataProvider = mockDataProvider;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  private Long getUserId() {
    //TODO: get user from auth context
    return mockDataProvider.getAllUsers().get(0).userId();
  }

  @GetMapping
  public ResponseEntity<?> getAllCompanies() {
    List<CompanyResponsePublicDTO> companies = companyService.getAllCompanies();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
    Long userId = getUserId();

    CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
  }

  @PostMapping
  public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto createRequestDto) {
    Long userId = getUserId();

    CompanyResponsePrivateDTO companyResponseDetails =
      companyService.createCompany(createRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "message", "Company created successfully",
      "data", companyResponseDetails));
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody
  CompanyUpdateRequestDto updateRequestDto) {
    Long userId = getUserId();

    CompanyResponsePrivateDTO companyResponseDetails =
      companyService.updateCompany(updateRequestDto, userId, companyId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message", "Company with ID " + companyId + " updated successfully",
      "data", companyResponseDetails));
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
    Long userId = getUserId();

    companyService.deleteCompany(companyId, userId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "Company with ID " + companyId + " deleted successfully"));
  }

  @GetMapping("/{companyId}/requests")
  public ResponseEntity<?> readJoinRequestsOfCompany(@PathVariable Long companyId) {
    Long userId = getUserId();

    List<CompanyJoinRequestResponseDto> requests =
      companyService.getJoinRequestsOfCompany(companyId, userId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "data", requests));
  }

  @PostMapping("/{companyId}/requests/join")
  public ResponseEntity<?> joinCompany(@PathVariable Long companyId) {
    Long userId = getUserId();

    CompanyJoinRequestResponseDto createdRequest = companyService.createJoinRequest(
      userId,
      companyId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "Request created successfully",
      "data", createdRequest));
  }

  @PutMapping("/{companyId}/requests/{requestId}")
  public ResponseEntity<?> updateJoinRequestById(
    @PathVariable Long requestId,
    @RequestBody CompanyJoinRequestUpdateDto requestDto) {
    Long userId = getUserId();

    CompanyJoinRequestResponseDto updatedRequestDto = companyService.updateJoinRequestById(userId
      , requestId, requestDto);

    //TODO: notify the user who requested to join...
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "Request updated successfully",
      "data", updatedRequestDto));
  }

  @ExceptionHandler(CompanyNotFoundException.class)
  public ResponseEntity<?> handleCompanyNotFound(CompanyNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "error", "The requested company was not found"));
  }

  @ExceptionHandler(UserAlreadyInCompanyException.class)
  public ResponseEntity<?> handleUserAlreadyInCompany(UserAlreadyInCompanyException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
      "error", "User is already in the requested company"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleDuplicateFields(ConstraintViolationException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
      "error", "Company with requested " + e.getConstraintName() + " already exists"));
  }
}