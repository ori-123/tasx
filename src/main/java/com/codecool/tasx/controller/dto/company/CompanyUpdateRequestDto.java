package com.codecool.tasx.controller.dto.company;

public record CompanyUpdateRequestDto(Long userId, Long companyId, String name,
                                      String description) {
}
