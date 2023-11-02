package com.codecool.tasx.controller.dto.task;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

public record TaskResponsePrivateDto(String description, long taskId,
                                     ProjectResponsePrivateDTO project,
                                     CompanyResponsePrivateDTO company,
                                     UserResponsePublicDto owner) {
}
