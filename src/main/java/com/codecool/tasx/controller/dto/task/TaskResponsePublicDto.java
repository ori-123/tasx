package com.codecool.tasx.controller.dto.task;

import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;

public record TaskResponsePublicDto(String description, CompanyResponsePublicDTO company) {
}
