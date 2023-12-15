package com.codecool.tasx.controller.dto.task;

import com.codecool.tasx.model.company.project.task.Importance;
import com.codecool.tasx.model.company.project.task.TaskStatus;
import com.codecool.tasx.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public record TaskUpdateRequestDto(String name, String description,
                                   Importance importance, Integer difficulty,
                                   LocalDateTime startDate,
                                   LocalDateTime deadline, TaskStatus taskStatus,
                                   Long points) {
}
