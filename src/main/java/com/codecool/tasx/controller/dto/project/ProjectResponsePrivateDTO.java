package com.codecool.tasx.controller.dto.project;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

public record ProjectResponsePrivateDTO(Long projectId, String name, String description,
                                        CompanyResponsePrivateDTO company,
                                        UserResponsePublicDto owner) {
}
