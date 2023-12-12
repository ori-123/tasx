package com.codecool.tasx.service.company.project.task;

import com.codecool.tasx.controller.dto.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.task.TaskUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.exception.task.TaskNotFoundException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.TaskConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
  private final TaskDao taskDao;
  private final ProjectDao projectDao;
  private final TaskConverter taskConverter;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public TaskService(
    TaskDao taskDao, ProjectDao projectDao, TaskConverter taskConverter, UserProvider userProvider,
    CustomAccessControlService accessControlService) {
    this.taskDao = taskDao;
    this.projectDao = projectDao;
    this.taskConverter = taskConverter;
    this.userProvider = userProvider;
    this.accessControlService = accessControlService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<TaskResponsePublicDto> getAllTasks(Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findById(projectId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(project.getCompany(), user);
    List<Task> tasks = project.getTasks();
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  public Optional<TaskResponsePublicDto> getTaskById(Long taskId) throws UnauthorizedException {
    Optional<Task> foundTask = taskDao.findById(taskId);
    if (foundTask.isEmpty()) {
      logger.error("Task with ID " + taskId + " was not found");
      return Optional.empty();
    }
    Task task = foundTask.get();
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(task.getProject().getCompany(), user);
    return Optional.of(taskConverter.getTaskResponsePublicDto(task));
  }

  @Transactional(rollbackOn = Exception.class)
  public TaskResponsePublicDto createTask(TaskCreateRequestDto createRequestDto, Long projectId)
    throws ConstraintViolationException {
    Project project = projectDao.findById(projectId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    Task task = new Task(createRequestDto.name(), createRequestDto.description(),
      createRequestDto.importance(), createRequestDto.difficulty(), createRequestDto.startDate(),
      createRequestDto.deadline(), createRequestDto.taskStatus(), user, project);
    task.assignEmployee(user);
    taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional(rollbackOn = Exception.class)
  public TaskResponsePublicDto updateTask(
    TaskUpdateRequestDto updateRequestDto, Long taskId) throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Task task = taskDao.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    accessControlService.verifyAssignedToProjectAccess(task.getProject(), user);
    task.setName(updateRequestDto.name());
    task.setDescription(updateRequestDto.description());
    task.setImportance(updateRequestDto.importance());
    task.setDifficulty(updateRequestDto.difficulty());
    task.setStartDate(updateRequestDto.startDate());
    task.setDeadline(updateRequestDto.deadline());
    task.setTaskStatus(updateRequestDto.taskStatus());
    Task savedTask = taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(savedTask);
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteTask(Long taskId) {
    User user = userProvider.getAuthenticatedUser();
    Task task = taskDao.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    accessControlService.verifyAssignedToProjectAccess(task.getProject(), user);
    taskDao.deleteById(taskId);
  }

}