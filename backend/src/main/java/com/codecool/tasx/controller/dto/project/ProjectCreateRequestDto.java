package com.codecool.tasx.controller.dto.project;

import java.time.LocalDateTime;

public record ProjectCreateRequestDto(String name, String description,
                                      LocalDateTime startDate, LocalDateTime deadline) {
}
