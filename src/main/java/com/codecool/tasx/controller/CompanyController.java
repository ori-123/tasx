package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.*;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final Logger logger;

  //TODO:logging, error handling

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping
  public ResponseEntity<?> getAllCompanies() {
    try {
      List<CompanyResponsePublicDTO> companies = companyService.getAll();
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
    } catch (Exception e) {
      //TODO: handle other exceptions
      logger.error(e.getMessage());
      return ResponseEntity.status(500).body(Map.of("error", "Failed to load companies"));
    }
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
    try {
      Optional<CompanyResponsePrivateDTO> company = companyService.getById(companyId);
      if (company.isEmpty()) {
        return handleCompanyNotFound(companyId);
      }
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
    } catch (Exception e) {
      //TODO: handle other exceptions
      logger.error(e.getMessage());
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }

  @PostMapping
  public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto companyDetails) {
    try {
      Long userId = 1L; //TODO: get this from actual auth context
      CompanyResponsePrivateDTO companyResponseDetails = companyService.create(
        userId, companyDetails);

      return ResponseEntity.status(HttpStatus.CREATED).body(
        Map.of("message", "Company created successfully", "data", companyResponseDetails));
    } catch (Exception e) {
      //TODO: handle other exceptions
      logger.error(e.getMessage());
      return ResponseEntity.status(500).body(Map.of("error", "Failed to create company"));
    }
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody CompanyUpdateRequestDto companyDetails) {
    try {
      Long userId = 1L; //TODO: get ID of actual authenticated user
      CompanyResponsePrivateDTO companyResponseDetails = companyService.update(companyId, userId,
        companyDetails);

      return ResponseEntity.status(HttpStatus.OK).body(
        Map.of("message", "Company with ID " + companyId + " updated successfully", "data",
          companyResponseDetails));
    } catch (CompanyNotFoundException e) {
      return handleCompanyNotFound(companyId);
    } catch (UnauthorizedException e) {
      return handleUnauthorized(e);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to update company with ID " + companyId));
    }
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
    try {
      Long userId = 1L; //TODO: get ID of actual authenticated user

      companyService.delete(companyId, userId);

      return ResponseEntity.status(HttpStatus.OK).body(
        Map.of("message", "Company with ID " + companyId + " deleted successfully"));
    } catch (CompanyNotFoundException e) {
      return handleCompanyNotFound(companyId);
    } catch (UnauthorizedException e) {
      return handleUnauthorized(e);
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
      CompanyJoinRequestDto joinRequest = new CompanyJoinRequestDto(companyId, userId);

      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Request to join company with ID " + companyId + " saved by user with ID " + userId));
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
        "Request to join company with ID " + companyId + " removed by user with ID " + 1L));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }

  private ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      Map.of("error", HttpStatus.UNAUTHORIZED.getReasonPhrase()));
  }

  private ResponseEntity<Map<String, String>> handleCompanyNotFound(Long companyId) {
    logger.error("Company with ID " + companyId + " was not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", HttpStatus.NOT_FOUND.getReasonPhrase()));
  }
}