package com.codecool.tasx.controller.dto.task;

import com.codecool.tasx.model.company.project.task.Importance;
import com.codecool.tasx.model.company.project.task.TaskStatus;

import java.time.LocalDateTime;

public record TaskUpdateRequestDto(Long userId, Long taskId, String name, String description,
                                   Importance importance, Integer difficulty, LocalDateTime startDate,
                                   LocalDateTime deadline, TaskStatus taskStatus) {
}
