package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePrivateDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.task.TaskUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> createTask(
            @PathVariable long companyId, @PathVariable long projectId,
            @RequestBody TaskCreateRequestDto taskDetails) {
        try {
            //TODO: impl
            TaskResponsePrivateDto taskResponseDetails = new TaskResponsePrivateDto(
                    taskDetails.description(), 4L, new ProjectResponsePrivateDTO(
                            projectId, "Mock project name", "Mock project description",
                    new CompanyResponsePrivateDTO(companyId, "Mock Company name", "Mock company description",
                            new UserResponsePublicDto(1L, "Company Owner")),
                    new UserResponsePublicDto(2L, "Project Owner")
                    ),
                    new CompanyResponsePrivateDTO(companyId, "Mock Company name", "Mock company description",
                            new UserResponsePublicDto(1L, "Company Owner")),
                    new UserResponsePublicDto(6L, "Task Owner")
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Task created successfully",
                    "data", taskResponseDetails));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to create task"));
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTaskById(
            @PathVariable long companyId, @PathVariable long projectId, @PathVariable long taskId,
            @RequestBody TaskUpdateRequestDto taskDetails) {
        try {
            TaskResponsePrivateDto taskResponseDetails = new TaskResponsePrivateDto(
                    taskDetails.description(), 4L, new ProjectResponsePrivateDTO(
                    projectId, "Mock project name", "Mock project description",
                    new CompanyResponsePrivateDTO(companyId, "Mock Company name", "Mock company description",
                            new UserResponsePublicDto(1L, "Company Owner")),
                    new UserResponsePublicDto(2L, "Project Owner")
            ),
                    new CompanyResponsePrivateDTO(companyId, "Mock Company name", "Mock company description",
                            new UserResponsePublicDto(1L, "Company Owner")),
                    new UserResponsePublicDto(6L, "Task Owner")
            );

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "Task with ID" + projectId + " updated successfully",
                    "data", taskResponseDetails));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to update task with ID " + taskId));
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable long companyId, @PathVariable long projectId, @PathVariable long taskId) {
        try {
            //TODO: impl
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message",
                    "Task with ID " + taskId + " deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to delete task with ID " + taskId));
        }
    }
}
