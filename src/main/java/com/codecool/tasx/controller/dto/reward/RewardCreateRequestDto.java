package com.codecool.tasx.controller.dto.reward;

import java.util.List;

public record RewardCreateRequestDto(Long userId, Long companyId, List<Long> taskIds, String name,
                                     String description) {
}
