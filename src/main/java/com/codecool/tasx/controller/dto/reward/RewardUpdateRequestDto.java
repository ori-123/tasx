package com.codecool.tasx.controller.dto.reward;

public record RewardUpdateRequestDto(Long userId, Long rewardId, String name, String description, int pointCost) {
}
