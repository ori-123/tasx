package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.*;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
  @GetMapping
  public ResponseEntity<?> getAllCompanies() {
    try {
      //TODO: impl
      List<CompanyResponsePublicDTO> companies = List.of(
        new CompanyResponsePublicDTO(Long.valueOf(1), "Mock Company 1",
          "Public company details"),
        new CompanyResponsePublicDTO(Long.valueOf(2), "Mock Company 2",
          "Public company details"));

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load companies"));
    }
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
    try {
      //TODO: impl
      CompanyResponsePrivateDTO company = new CompanyResponsePrivateDTO(
        Long.valueOf(companyId),
        "Mock Company " + companyId,
        "Company details to be seen only by the employees of the company",
        new UserResponsePublicDto(Long.valueOf(1), "Company Owner"));

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }

  @PostMapping
  public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto companyDetails) {
    try {
      //TODO: impl
      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Company created successfully"));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to create company"));
    }
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody
  CompanyUpdateRequestDto companyDetails) {
    try {
      //TODO: impl
      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Company with ID " + companyId + "updated successfully"));
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
      long userId = 1;
      CompanyJoinRequestDto joinRequest = new CompanyJoinRequestDto(companyId, userId);

      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Request to join company with ID " + companyId +
          " saved by user with ID " + userId));
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
      long userId = 1;

      return ResponseEntity.status(HttpStatus.OK).body(Map.of(
        "message",
        "Request to join company with ID " + companyId +
          " removed by user with ID " + userId));
    } catch (Exception e) {
      //TODO: handle other exceptions
      return ResponseEntity.status(500).body(
        Map.of("error", "Failed to load details of company with ID " + companyId));
    }
  }
}