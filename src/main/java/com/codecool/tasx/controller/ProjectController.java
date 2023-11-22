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
  private final Logger logger;

  @Autowired
  public ProjectController(
    CompanyService companyService, ProjectService projectService) {
    this.companyService = companyService;
    this.projectService = projectService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping
  public ResponseEntity<?> getAllProjects(
    @PathVariable Long companyId) {
    CompanyResponsePrivateDTO company = companyService.getCompanyById(companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
    List<ProjectResponsePublicDTO> projects = projectService.getAllProjects(company.companyId());
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(
    @PathVariable Long companyId, @PathVariable Long projectId) {
    CompanyResponsePrivateDTO company = companyService.getCompanyById(companyId)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
    ProjectResponsePrivateDTO project = projectService.getProjectById(
      projectId, company.companyId()).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", project));
  }

  @PostMapping
  public ResponseEntity<?> createProject(
    @PathVariable Long companyId,
    @RequestBody ProjectCreateRequestDto projectDetails) {
    ProjectResponsePrivateDTO projectResponseDetails = projectService.createProject(
      projectDetails, companyId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Project created successfully", "data", projectResponseDetails));
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<?> updateProject(
    @PathVariable Long projectId,
    @RequestBody ProjectUpdateRequestDto projectDetails) {
    ProjectResponsePrivateDTO projectResponseDetails = projectService.updateProject(
      projectDetails, projectId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " updated successfully", "data",
        projectResponseDetails));
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
    projectService.deleteProject(projectId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Project with ID " + projectId + " deleted successfully"));
  }
}
