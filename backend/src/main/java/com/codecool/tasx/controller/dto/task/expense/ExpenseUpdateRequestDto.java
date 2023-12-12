package com.codecool.tasx.controller.dto.task.expense;

public record ExpenseUpdateRequestDto(String name, Double price, Boolean paid) {
}
