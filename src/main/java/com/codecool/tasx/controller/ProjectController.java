package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.service.populate.MockDataProvider;
import com.codecool.tasx.service.project.ProjectService;
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
    private final ProjectService projectService;
    private final MockDataProvider mockDataProvider;
    private final Logger logger;

    @Autowired
    public ProjectController(ProjectService projectService, MockDataProvider mockDataProvider) {
        this.projectService = projectService;
        this.mockDataProvider = mockDataProvider;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    private Long getUserId() {
        //TODO: get user from auth context
        return mockDataProvider.getAllUsers().get(0).userId();
    }

      @GetMapping
      public ResponseEntity<?> getAllProjects(@PathVariable Long companyId) {
        List<ProjectResponsePublicDTO> projects = projectService.getAllProjects();
          return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
      }

      @GetMapping("/{projectId}")
      public ResponseEntity<?> getProjectById(
        @PathVariable Long companyId,
        @PathVariable Long projectId) {
        Long userId = getUserId();
        ProjectResponsePrivateDTO project = projectService.getProjectById(userId, projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
          return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", project));
      }

      @PostMapping
      public ResponseEntity<?> createProject(
        @PathVariable Long companyId,
        @RequestBody ProjectCreateRequestDto projectDetails) {
        try {
          //TODO: impl
          ProjectResponsePrivateDTO projectResponseDetails = new ProjectResponsePrivateDTO(
            1L, projectDetails.name(), projectDetails.description(),
            new CompanyResponsePrivateDTO(companyId, "Mock company " + companyId,
              "Company details to be seen only by the employees of the company",
              new UserResponsePublicDto(1L, "Company Owner")),
            new UserResponsePublicDto(2L, "Project Owner"));

          return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Project created successfully",
            "data", projectResponseDetails));
        } catch (Exception e) {
          //TODO: handle other exceptions
          return ResponseEntity.status(500).body(
            Map.of("error", "Failed to create project"));
        }
      }

      @PutMapping("/{projectId}")
      public ResponseEntity<?> updateProject(
        @PathVariable Long companyId, @PathVariable Long projectId, @RequestBody
      ProjectUpdateRequestDto projectDetails) {
        try {
          //TODO: impl
          ProjectResponsePrivateDTO projectResponseDetails = new ProjectResponsePrivateDTO(
            projectId, projectDetails.name(), projectDetails.description(),
            new CompanyResponsePrivateDTO(companyId, "Mock company " + companyId,
              "Company details to be seen only by the employees of the company",
              new UserResponsePublicDto(1L, "Company Owner")),
            new UserResponsePublicDto(2L, "Project Owner"));

          return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "message", "Project with ID " + projectId + " updated successfully",
            "data", projectResponseDetails));
        } catch (Exception e) {
          //TODO: handle other exceptions
          return ResponseEntity.status(500).body(
            Map.of("error", "Failed to update project with ID " + projectId));
        }
      }

      @DeleteMapping("/{projectId}")
      public ResponseEntity<?> deleteProject(
        @PathVariable Long companyId, @PathVariable Long projectId) {
        try {
          //TODO: impl
          return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "message",
            "Project with ID " + projectId + " deleted successfully"));
        } catch (Exception e) {
          //TODO: handle other exceptions
          return ResponseEntity.status(500).body(
            Map.of("error", "Failed to delete project with ID " + projectId));
        }
      }
}
