package com.codecool.tasx.service.company.project.task;

import com.codecool.tasx.controller.dto.company.project.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.company.project.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.company.project.task.TaskUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.company.project.task.TaskAlreadyFinalizedException;
import com.codecool.tasx.exception.company.project.task.TaskNotFoundException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.company.project.task.TaskStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
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
import java.util.Set;

@Service
public class TaskService {
  private static final Set<TaskStatus> finishedTaskStatuses = Set.of(
    TaskStatus.DONE,
    TaskStatus.FAILED);
  private final TaskDao taskDao;
  private final ProjectDao projectDao;
  private final UserDao userDao;
  private final TaskConverter taskConverter;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public TaskService(
    TaskDao taskDao, ProjectDao projectDao, UserDao userDao, TaskConverter taskConverter,
    UserProvider userProvider, CustomAccessControlService accessControlService) {
    this.taskDao = taskDao;
    this.projectDao = projectDao;
    this.userDao = userDao;
    this.taskConverter = taskConverter;
    this.userProvider = userProvider;
    this.accessControlService = accessControlService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<TaskResponsePublicDto> getAllTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(companyId, projectId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(project.getCompany(), user);
    List<Task> tasks = project.getTasks();
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  public TaskResponsePublicDto getTaskById(Long companyId, Long projectId, Long taskId)
    throws UnauthorizedException {
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(task.getProject().getCompany(), user);
    return taskConverter.getTaskResponsePublicDto(task);
  }

  @Transactional
  public List<TaskResponsePublicDto> getTasksByStatus(
    Long companyId, Long projectId, TaskStatus status)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatus(project, status);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  public List<TaskResponsePublicDto> getFinishedTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatusIn(project, finishedTaskStatuses);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional
  public List<TaskResponsePublicDto> getUnfinishedTasks(Long companyId, Long projectId)
    throws ProjectNotFoundException, UnauthorizedException {
    Project project = projectDao.findByIdAndCompanyId(projectId, companyId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    List<Task> tasks = taskDao.findAllByProjectAndTaskStatusNotIn(project, finishedTaskStatuses);
    return taskConverter.getTaskResponsePublicDtos(tasks);
  }

  @Transactional(rollbackOn = Exception.class)
  public TaskResponsePublicDto createTask(
    TaskCreateRequestDto createRequestDto, Long companyId, Long projectId)
    throws ConstraintViolationException {
    Project project = projectDao.findByIdAndCompanyId(companyId, projectId).orElseThrow(
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
    TaskUpdateRequestDto updateRequestDto, Long companyId, Long projectId, Long taskId)
    throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    verifyEditable(task);
    accessControlService.verifyAssignedToProjectAccess(task.getProject(), user);
    updateTaskDetails(updateRequestDto, task);
    if (updateRequestDto.taskStatus().equals(TaskStatus.DONE)) {
      acquirePointsForTask(task);
    }
    Task savedTask = taskDao.save(task);
    return taskConverter.getTaskResponsePublicDto(savedTask);
  }

  private void acquirePointsForTask(Task task) throws UnauthorizedException {
    List<User> assignedEmployees = task.getAssignedEmployees();
    for (User employee : assignedEmployees) {
      employee.setScore(employee.getScore() + task.calculatePoints());
      userDao.save(employee);
    }
  }

  private void verifyEditable(Task task) {
    if (finishedTaskStatuses.contains(task.getTaskStatus())) {
      throw new TaskAlreadyFinalizedException(task.getId());
    }
  }

  private void updateTaskDetails(TaskUpdateRequestDto updateRequestDto, Task task) {
    task.setName(updateRequestDto.name());
    task.setDescription(updateRequestDto.description());
    task.setImportance(updateRequestDto.importance());
    task.setDifficulty(updateRequestDto.difficulty());
    task.setStartDate(updateRequestDto.startDate());
    task.setDeadline(updateRequestDto.deadline());
    task.setTaskStatus(updateRequestDto.taskStatus());
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteTask(Long companyId, Long projectId, Long taskId) {
    User user = userProvider.getAuthenticatedUser();
    Task task = taskDao.findByCompanyIdAndProjectIdAndTaskId(companyId, projectId, taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));
    accessControlService.verifyAssignedToProjectAccess(task.getProject(), user);
    taskDao.delete(task);
  }

}
