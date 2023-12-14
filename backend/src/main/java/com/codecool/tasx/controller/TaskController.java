package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.task.TaskUpdateRequestDto;
import com.codecool.tasx.exception.task.TaskNotFoundException;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskStatus;
import com.codecool.tasx.model.user.User;
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
  public ResponseEntity<?> getAllTasks(@PathVariable Long projectId) {
    List<TaskResponsePublicDto> tasks = taskService.getAllTasks(projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
  }

  @GetMapping("/finishedtasks")
  public ResponseEntity<?> getFinishedTasks(@PathVariable Long projectId) {
    List<TaskResponsePublicDto> tasks = taskService.getFinishedTasks(projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
  }

  @GetMapping("/unfinishedtasks")
  public ResponseEntity<?> getUnfinishedTasks(@PathVariable Long projectId) {
    List<TaskResponsePublicDto> tasks = taskService.getUnfinishedTasks(projectId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", tasks));
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
    TaskResponsePublicDto task = taskService.getTaskById(taskId).orElseThrow(
      () -> new TaskNotFoundException(taskId));
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", task));
  }

  @PostMapping
  public ResponseEntity<?> createTask(
    @PathVariable Long projectId, @RequestBody TaskCreateRequestDto taskDetails) {
    TaskResponsePublicDto taskResponseDetails = taskService.createTask(taskDetails, projectId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Task created successfully", "data", taskResponseDetails));
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<?> updateTask(
    @PathVariable Long taskId, @RequestBody TaskUpdateRequestDto taskDetails) {
    if (taskDetails.taskStatus().equals(TaskStatus.DONE)) {
      throw new IllegalArgumentException();
    }

    TaskResponsePublicDto taskResponseDetails = taskService.updateTask(taskDetails, taskId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Task with ID " + taskId + " updated successfully", "data",
        taskResponseDetails));
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
    taskService.deleteTask(taskId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Task with ID " + taskId + " deleted successfully"));
  }
}
