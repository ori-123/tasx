package com.codecool.tasx.controller.dto.reward;

import com.codecool.tasx.model.company.project.task.Task;

import java.util.List;

public record RewardResponseDto(Long companyId, Long rewardId, String name,
                                String description, List<Task> tasks) {
}
