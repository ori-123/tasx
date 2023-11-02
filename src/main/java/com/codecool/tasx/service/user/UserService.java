package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.user.UserCreateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

import java.util.Optional;

public interface UserService {
  //TODO: impl
  void create(UserCreateRequestDto userDetails);
  Optional<UserResponsePublicDto> findById(Long userId);
}
