package com.codecool.tasx.controller.dto.company;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

public record CompanyResponsePrivateDTO(Long id, String name, String description,
                                        UserResponsePublicDto owner) {
}
