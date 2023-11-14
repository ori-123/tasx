package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.service.company.CompanyService;
import com.codecool.tasx.service.populate.MockDataProvider;
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

  @GetMapping
  public ResponseEntity<?> getAllCompanies() {
    List<CompanyResponsePublicDTO> companies = companyService.getAllCompanies();
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
    //TODO: get user from auth context
    Long userId = mockDataProvider.getAllUsers().get(0).userId();
    CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
  }

/*
  @PostMapping
  public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto companyDetails) {
    //TODO: impl
    CompanyResponsePrivateDTO companyResponseDetails =
        new CompanyResponsePrivateDTO(1L, companyDetails.name(), companyDetails.description(),
          new UserResponsePublicDto(1L, "Company Owner"));

      return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "message", "Company created successfully",
        "data", companyResponseDetails));
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody
  CompanyUpdateRequestDto companyDetails) {
    try {
      //TODO: impl
      CompanyResponsePrivateDTO companyResponseDetails =
        new CompanyResponsePrivateDTO(companyId, companyDetails.name(), companyDetails.description(),
          new UserResponsePublicDto(1L, "Company Owner"));

      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message","Company with ID " + companyId + " updated successfully",
        "data",companyResponseDetails));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to update company with ID " + companyId));
    }
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
    try {
      //TODO: impl
      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Company with ID " + companyId + " deleted successfully"));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to delete company with ID " + companyId));
    }
  }

  @GetMapping("/{companyId}/join")
  public ResponseEntity<?> joinCompany(@PathVariable Long companyId) {
    try {
      //TODO: impl
      Long userId = 1L;
      CompanyJoinRequestCreateDto joinRequest = new CompanyJoinRequestCreateDto(companyId, userId);

      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Request to join company with ID " + companyId +
          " saved by populate with ID " + userId));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }


  @DeleteMapping("/{companyId}/join")
  public ResponseEntity<?> removeJoinCompanyRequest(@PathVariable Long companyId) {
    try {
      //TODO: impl
      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Request to join company with ID " + companyId +
          " removed by populate with ID " + 1L));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }
  */

  @ExceptionHandler(CompanyNotFoundException.class)
  public ResponseEntity<?> handleCompanyNotFound(CompanyNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "error", "The requested company was not found"));
  }
}