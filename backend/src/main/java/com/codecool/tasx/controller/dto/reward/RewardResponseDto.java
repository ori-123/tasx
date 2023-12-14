package com.codecool.tasx.controller.dto.reward;

import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;

import java.util.List;

public record RewardResponseDto(Long companyId, Long rewardId, String name,
                                String description, List<TaskResponsePublicDto> tasks, long pointCost) {
}
