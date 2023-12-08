package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.task.expense.ExpenseCreateRequestDto;
import com.codecool.tasx.controller.dto.task.expense.ExpenseResponseDto;
import com.codecool.tasx.controller.dto.task.expense.ExpenseUpdateRequestDto;
import com.codecool.tasx.exception.expense.ExpenseNotFoundException;
import com.codecool.tasx.service.company.project.task.expense.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks/{taskId}")
public class ExpenseController {
  private final ExpenseService expenseService;

  @Autowired
  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @GetMapping
  public ResponseEntity<?> getAllExpensesOfTask(@PathVariable Long taskId) {
    List<ExpenseResponseDto> expenses = expenseService.getAllExpenses(taskId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", expenses));
  }

  @GetMapping("/{expenseId}")
  public ResponseEntity<?> getExpenseById(@PathVariable Long expenseId) {
    ExpenseResponseDto expense = expenseService.getExpense(expenseId).orElseThrow(
      () -> new ExpenseNotFoundException());
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", expense));
  }

  @PostMapping
  public ResponseEntity<?> createExpense(
    @PathVariable Long taskId,
    @RequestBody ExpenseCreateRequestDto createRequestDto) {
    ExpenseResponseDto expense = expenseService.createExpense(
      createRequestDto,
      taskId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Expense created successfully", "data", expense));
  }

  @PutMapping("/{expenseId}")
  public ResponseEntity<?> updateExpense(
    @PathVariable Long expenseId,
    @RequestBody ExpenseUpdateRequestDto updateRequestDto) {
    ExpenseResponseDto expense = expenseService.updateExpense(
      updateRequestDto, expenseId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Expense with ID " + expenseId + " updated successfully", "data",
        expense));
  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId) {
    expenseService.deleteExpense(expenseId);

    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Expense with ID " + expenseId + " deleted successfully"));
  }
}
