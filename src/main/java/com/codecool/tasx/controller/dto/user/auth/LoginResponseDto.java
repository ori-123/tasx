package com.codecool.tasx.controller.dto.user.auth;

import com.codecool.tasx.model.user.Role;

import java.util.Set;

public record LoginResponseDto(String accessToken, UserInfoDto userInfo) {
  public record UserInfoDto(String username, String email, Set<Role> roles) {
  }
}
