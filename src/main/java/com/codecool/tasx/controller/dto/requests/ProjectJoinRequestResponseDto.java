package com.codecool.tasx.controller.dto.requests;

import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.requests.RequestStatus;

public record ProjectJoinRequestResponseDto(
        Long requestId, ProjectResponsePublicDTO company, UserResponsePublicDto user,
        RequestStatus status
) {
}
