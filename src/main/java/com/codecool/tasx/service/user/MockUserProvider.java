package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MockUserProvider {
  private final UserDao userDao;

  @Autowired
  public MockUserProvider(UserDao userDao) {
    this.userDao = userDao;
  }

  public void populate() {
    User user1 = new User("User1", "asd.asd@asd.asd", "asdasdasd");
    userDao.save(user1);
    User user2 = new User("User2", "asdfg.asd@asd.asd", "asdfgasdasd");
    userDao.save(user2);
  }

  public List<UserResponsePublicDto> getAll() {
    List<User> users = userDao.findAll();
    return users.stream().map(user -> new UserResponsePublicDto(user.getId(), user.getUsername()))
      .collect(Collectors.toList());
  }

  public Optional<UserResponsePublicDto> getById(Long userId) {
    Optional<User> foundUser = userDao.findById(userId);
    if (foundUser.isPresent()) {
      return Optional.of(
        new UserResponsePublicDto(foundUser.get().getId(), foundUser.get().getUsername()));
    } else {
      return Optional.empty();
    }
  }
}
