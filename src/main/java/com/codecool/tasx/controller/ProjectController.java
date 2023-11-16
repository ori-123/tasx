package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectUpdateRequestDto;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.service.company.CompanyService;
import com.codecool.tasx.service.company.project.ProjectService;
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
@RequestMapping("/api/v1/companies/{companyId}/projects")
public class ProjectController {
  private final CompanyService companyService;
  private final ProjectService projectService;
  private final AuthProvider authProvider;
  private final Logger logger;

  @Autowired
  public ProjectController(
    CompanyService companyService, ProjectService projectService, AuthProvider authProvider) {
    this.companyService = companyService;
    this.projectService = projectService;
    this.authProvider = authProvider;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping
  public ResponseEntity<?> getAllProjects(
    @PathVariable Long companyId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);
    CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
    List<ProjectResponsePublicDTO> projects = projectService.getAllProjects(
      company.companyId(),
      userId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(
    @PathVariable Long companyId, @PathVariable Long projectId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);
    CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
    ProjectResponsePrivateDTO project = projectService.getProjectById(
      userId, projectId, company.companyId()).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", project));
  }

  @PostMapping
  public ResponseEntity<?> createProject(
    @PathVariable Long companyId,
    @RequestBody ProjectCreateRequestDto projectDetails,
    HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    ProjectResponsePrivateDTO projectResponseDetails = projectService.createProject(
      projectDetails, userId, companyId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Project created successfully", "data", projectResponseDetails));
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<?> updateProject(
    @PathVariable Long projectId,
    @RequestBody ProjectUpdateRequestDto projectDetails, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);

    ProjectResponsePrivateDTO projectResponseDetails = projectService.updateProject(
      projectDetails, userId, projectId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " updated successfully", "data",
        projectResponseDetails));
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(@PathVariable Long projectId, HttpServletRequest request) {
    Long userId = authProvider.getUserId(request);
    projectService.deleteProject(projectId, userId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " deleted successfully"));
  }
}
