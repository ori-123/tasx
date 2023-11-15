package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.service.company.CompanyService;
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
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final MockDataProvider mockDataProvider;
    private final Logger logger;

    @Autowired
    public ProjectController(CompanyService companyService, ProjectService projectService, MockDataProvider mockDataProvider) {
        this.companyService = companyService;
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
        Long userId = getUserId();
        CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
        List<ProjectResponsePublicDTO> projects = projectService.getAllProjects(company.companyId());
          return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", projects));
      }

      @GetMapping("/{projectId}")
      public ResponseEntity<?> getProjectById(@PathVariable Long companyId, @PathVariable Long projectId) {
        Long userId = getUserId();
          CompanyResponsePrivateDTO company = companyService.getCompanyById(userId, companyId)
                  .orElseThrow(() -> new CompanyNotFoundException(companyId));
        ProjectResponsePrivateDTO project = projectService.getProjectById(userId, projectId, company.companyId())
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
          return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", project));
      }

      @PostMapping
      public ResponseEntity<?> createProject(
        @PathVariable Long companyId,
        @RequestBody ProjectCreateRequestDto projectDetails) {
        //TODO: impl
      }

      @PutMapping("/{projectId}")
      public ResponseEntity<?> updateProject(
        @PathVariable Long companyId, @PathVariable Long projectId, @RequestBody
      ProjectUpdateRequestDto projectDetails) {
        //TODO: impl
      }

      @DeleteMapping("/{projectId}")
      public ResponseEntity<?> deleteProject(
        @PathVariable Long companyId, @PathVariable Long projectId) {
        //TODO: impl
      }
}
