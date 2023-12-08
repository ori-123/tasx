package com.codecool.tasx.service.company.project.task.expense;

import com.codecool.tasx.controller.dto.task.expense.ExpenseCreateRequestDto;
import com.codecool.tasx.controller.dto.task.expense.ExpenseResponseDto;
import com.codecool.tasx.controller.dto.task.expense.ExpenseUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.expense.ExpenseNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.exception.task.TaskNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Expense;
import com.codecool.tasx.model.company.project.task.ExpenseDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
  private final TaskDao taskDao;
  private final ExpenseDao expenseDao;
  private final ProjectDao projectDao;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public ExpenseService(
    TaskDao taskDao, ExpenseDao expenseDao, ProjectDao projectDao, UserProvider userProvider,
    CustomAccessControlService accessControlService) {
    this.taskDao = taskDao;
    this.expenseDao = expenseDao;
    this.projectDao = projectDao;
    this.userProvider = userProvider;
    this.accessControlService = accessControlService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public List<ExpenseResponseDto> getAllExpenses(Long taskId)
    throws ProjectNotFoundException, UnauthorizedException {
    Task task = taskDao.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    User user = userProvider.getAuthenticatedUser();
    Company company = task.getProject().getCompany();
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    List<Expense> expenses = task.getExpenses();
    return expenses.stream().map(
      expense -> new ExpenseResponseDto(expense.getId(), expense.getName(), expense.getPrice(),
        expense.isPaid())).collect(Collectors.toList());
  }

  public Optional<ExpenseResponseDto> getExpense(Long expenseId) throws UnauthorizedException {
    Optional<Expense> expense = expenseDao.findById(expenseId);
    if (expense.isEmpty()) {
      return Optional.empty();
    }
    User user = userProvider.getAuthenticatedUser();
    Company company = expense.get().getTask().getProject().getCompany();
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    return Optional.of(new ExpenseResponseDto(expense.get().getId(), expense.get().getName(),
      expense.get().getPrice(), expense.get().isPaid()));
  }

  @Transactional(rollbackOn = Exception.class)
  public ExpenseResponseDto createExpense(ExpenseCreateRequestDto createRequestDto, Long taskId)
    throws ConstraintViolationException {
    Task task = taskDao.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    User user = userProvider.getAuthenticatedUser();
    Project project = task.getProject();
    accessControlService.verifyAssignedToProjectAccess(project, user);
    Expense expense = new Expense(createRequestDto.name(), createRequestDto.price(),
      createRequestDto.paid(), task);
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  public ExpenseResponseDto updateExpense(
    ExpenseUpdateRequestDto updateRequestDto, Long expenseId) throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Expense expense = expenseDao.findById(expenseId).orElseThrow(
      () -> new ExpenseNotFoundException());
    Project project = projectDao.findById(expense.getTask().getProject().getId()).orElseThrow(
      () -> new ProjectNotFoundException(expense.getTask().getId()));
    accessControlService.verifyAssignedToProjectAccess(project, user);
    expense.setName(updateRequestDto.name());
    expense.setPrice(updateRequestDto.price());
    expense.setPaid(updateRequestDto.paid());
    Expense savedExpense = expenseDao.save(expense);
    return new ExpenseResponseDto(savedExpense.getId(), savedExpense.getName(),
      savedExpense.getPrice(), savedExpense.isPaid());
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteExpense(Long expenseId) {
    User user = userProvider.getAuthenticatedUser();
    Expense expense = expenseDao.findById(expenseId).orElseThrow(
      () -> new ExpenseNotFoundException());
    Project project = projectDao.findById(expense.getTask().getProject().getId()).orElseThrow(
      () -> new ProjectNotFoundException(expense.getTask().getProject().getId()));
    accessControlService.verifyAssignedToProjectAccess(project, user);
    expenseDao.delete(expense);
  }

}
