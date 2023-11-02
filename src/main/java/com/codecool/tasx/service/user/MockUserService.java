package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.user.UserCreateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MockUserService implements UserService {
  private final UserDao userDao;

  @Autowired
  public MockUserService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void create(UserCreateRequestDto userDetails) {
    User user = new User();
    user.setUsername(userDetails.username());
    userDao.save(user);
  }

  @Override
  public Optional<UserResponsePublicDto> findById(Long userId) {
    Optional<User> user = userDao.findById(userId);
    if (user.isPresent()) {
      return Optional.of(new UserResponsePublicDto(user.get().getId(), user.get().getUsername()));
    }
    return Optional.empty();
  }
}
