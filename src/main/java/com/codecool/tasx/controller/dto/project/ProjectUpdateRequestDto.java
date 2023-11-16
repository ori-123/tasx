package com.codecool.tasx.controller.dto.project;

import java.time.LocalDateTime;

public record ProjectUpdateRequestDto(String name,
                                      String description, LocalDateTime deadline) {
}
