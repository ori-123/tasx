package com.codecool.tasx.controller.dto.project;

import java.time.LocalDateTime;

public record ProjectUpdateRequestDto(Long userId, Long projectId, String name,
                                      String description, LocalDateTime deadline) {
}
