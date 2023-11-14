package com.codecool.tasx.controller.dto.company;

import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

import java.util.List;

public record CompanyResponsePrivateDTO(
  Long companyId, String name, String description,
  UserResponsePublicDto companyOwner,
  List<UserResponsePublicDto> employees,
  List<ProjectResponsePublicDTO> projects,
  List<RewardResponseDto> rewards
) {
}
