package com.codecool.tasx.controller.dto.task.expense;

public record ExpenseUpdateRequestDto(Long expenseId, String name, Double price, Boolean paid) {
}
