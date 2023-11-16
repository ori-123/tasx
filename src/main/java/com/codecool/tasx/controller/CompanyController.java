package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.service.company.CompanyService;
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
@RequestMapping("/api/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final AuthProvider authProvider;
  private final Logger logger;

  @Autowired
  public CompanyController(CompanyService companyService, AuthProvider authProvider) {
    this.companyService = companyService;
    this.authProvider = authProvider;
    logger = LoggerFactory.getLogger(this.getClass());
  }


  @GetMapping()
  public ResponseEntity<?> getAllCompanies(
    @RequestParam(name = "withUser") Boolean withUser, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);
    List<CompanyResponsePublicDTO> companies;
    if (withUser) {
      companies = companyService.getCompaniesWithUserId(userId);
    } else {
      companies = companyService.getCompaniesWithoutUserId(userId);
    }
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", companies));
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(
    @PathVariable Long companyId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", company));
  }

  @PostMapping
  public ResponseEntity<?> createCompany(
    @RequestBody CompanyCreateRequestDto createRequestDto,
    HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    CompanyResponsePrivateDTO companyResponseDetails =
      companyService.createCompany(createRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "message", "Company created successfully",
      "data", companyResponseDetails));
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
    @PathVariable Long companyId, @RequestBody
  CompanyUpdateRequestDto updateRequestDto, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    CompanyResponsePrivateDTO companyResponseDetails =
      companyService.updateCompany(updateRequestDto, userId, companyId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message", "Company with ID " + companyId + " updated successfully",
      "data", companyResponseDetails));
  }

  @DeleteMapping("/{companyId}")
  public ResponseEntity<?> deleteCompany(@PathVariable Long companyId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    companyService.deleteCompany(companyId, userId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "Company with ID " + companyId + " deleted successfully"));
  }
}