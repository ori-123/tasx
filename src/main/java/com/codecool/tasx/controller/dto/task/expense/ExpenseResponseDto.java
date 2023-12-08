package com.codecool.tasx.controller.dto.task.expense;

public record ExpenseResponseDto(Long expenseId, String name, Double price, Boolean paid) {
}
