package com.codecool.tasx.controller.dto.project;

public record ProjectUpdateRequestDto(Long userId, Long projectId, String name,
                                      String description) {
}
