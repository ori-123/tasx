package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.task.TaskResponsePrivateDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks")
public class TaskController {

    @GetMapping
    public ResponseEntity<?> getAllTasks(@PathVariable long companyId, @PathVariable long projectId) {
        try {
            //TODO: impl
            List<TaskResponsePublicDto> tasks = List.of(
                    new TaskResponsePublicDto("Task description 1", new CompanyResponsePublicDTO(
                            companyId, "Mock Company 1", "Public company details")),
                    new TaskResponsePublicDto("Task description 2", new CompanyResponsePublicDTO(
                            companyId, "Mock Company 1", "Public company details"))
            );

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to load tasks"));
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable long companyId, @PathVariable long projectId,
                                         @PathVariable long taskId) {
        try {
            TaskResponsePrivateDto task = new TaskResponsePrivateDto(
                    "Task details",
                    taskId,
                    new ProjectResponsePrivateDTO(
                            projectId,
                            "Mock prject name" + projectId,
                            "Project details",
                            new CompanyResponsePrivateDTO(companyId, "Mock company " + companyId,
                                    "Company details to be seen only by the employees of the company",
                                    new UserResponsePublicDto(1L, "Company Owner")),
                            new UserResponsePublicDto(2L, "Project owner")
                    ), new CompanyResponsePrivateDTO(companyId, "Mock company " + companyId,
                    "Company details to be seen only by the employees of the company",
                    new UserResponsePublicDto(1L, "Company Owner")),
                    new UserResponsePublicDto(3L, "Task owner")
            );

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", task));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error",
                    "Failed to load task with ID " + taskId)
            );
        }
    }
}
