package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.task.TaskUpdateRequestDto;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.exception.task.TaskNotFoundException;
import com.codecool.tasx.service.company.project.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(@PathVariable Long companyId, @PathVariable Long projectId) {
        List<TaskResponsePublicDto> tasks = taskService.getAllTasks(projectId, companyId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long companyId, @PathVariable Long projectId, @PathVariable Long taskId) {
        TaskResponsePublicDto task = taskService.getTaskById(taskId, projectId, companyId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", task));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@PathVariable Long companyId, @PathVariable Long projectId,
                                        @RequestBody TaskCreateRequestDto taskDetails) {
        TaskResponsePublicDto taskResponseDetails = taskService.createTask(taskDetails, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", "Task created successfully", "data", taskResponseDetails));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long projectId, @PathVariable Long taskId,
                                        @RequestBody TaskUpdateRequestDto taskDetails) {
        TaskResponsePublicDto taskResponseDetails = taskService.updateTask(taskDetails, taskId, projectId);

        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message", "Task with ID " + taskId + " updated successfully", "data", taskResponseDetails));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTask(taskId, projectId);

        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("message", "Task with ID " + taskId + " deleted successfully"));
    }
}
