package com.codecool.tasx.service.converter;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserConverter {

  public UserResponsePublicDto getUserResponsePublicDto(User user) {
    return new UserResponsePublicDto(user.getId(), user.getActualUsername(), user.getScore());
  }

  public List<UserResponsePublicDto> getUserResponsePublicDtos(List<User> users) {
    return users.stream().map(user -> getUserResponsePublicDto(user)).collect(Collectors.toList());
  }
}
