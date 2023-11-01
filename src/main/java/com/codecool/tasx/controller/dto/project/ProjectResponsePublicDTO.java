package com.codecool.tasx.controller.dto.project;

import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;

public record ProjectResponsePublicDTO(Long projectId, String name,
                                       String description,
                                       CompanyResponsePublicDTO company) {
}
