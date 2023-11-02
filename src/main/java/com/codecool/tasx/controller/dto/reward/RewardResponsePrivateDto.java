package com.codecool.tasx.controller.dto.reward;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

import java.util.Set;

public record RewardResponsePrivateDto(long rewardId, String name, String description,
                                       CompanyResponsePrivateDTO company, Set<UserResponsePublicDto> employees) {
}
