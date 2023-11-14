package com.codecool.tasx.service.populate;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.*;
import com.codecool.tasx.model.company.reward.RewardDao;
import com.codecool.tasx.model.requests.CompanyJoinRequestDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MockDataProvider {
  private final UserDao userDao;
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;
  private final ExpenseDao expenseDao;
  private final RewardDao rewardDao;
  private final CompanyJoinRequestDao joinRequestDao;

  @Autowired
  public MockDataProvider(
    UserDao userDao, CompanyDao companyDao, ProjectDao projectDao, TaskDao taskDao,
    ExpenseDao expenseDao, RewardDao rewardDao, CompanyJoinRequestDao joinRequestDao) {
    this.userDao = userDao;
    this.companyDao = companyDao;
    this.projectDao = projectDao;
    this.taskDao = taskDao;
    this.expenseDao = expenseDao;
    this.rewardDao = rewardDao;
    this.joinRequestDao = joinRequestDao;
  }

  public void populate() {
    User user1 = new User("User1", "asd.asd@asd.asd", "asdasdasd");
    userDao.save(user1);
    User user2 = new User("User2", "asdfg.asd@asd.asd", "asdfgasdasd");
    userDao.save(user2);

    Company company1 = new Company("Company1", "Company 1", user1);
    companyDao.save(company1);
    Company company2 = new Company("Company2", "Company 2", user2);
    companyDao.save(company2);

    Project project1 = new Project("Project1", "project 1", LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), user1, company1);
    projectDao.save(project1);
    Project project2 = new Project("Project2", "project 2", LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), user2, company2);
    projectDao.save(project2);

    Task task1 = new Task("Task 1", "asdasd", Importance.NICE_TO_HAVE, 1, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.BACKLOG, user1, project1);
    taskDao.save(task1);
    Task task2 = new Task("Task 2", "asdasd", Importance.MUST_HAVE, 1, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.IN_PROGRESS, user1, project1);
    taskDao.save(task2);
    Task task3 = new Task("Task 3", "asdasd", Importance.NICE_TO_HAVE, 5, LocalDateTime.now(),
      LocalDateTime.now().plusHours(1), TaskStatus.BACKLOG, user2, project2);
    taskDao.save(task3);
  }

  public List<UserResponsePublicDto> getAllUsers() {
    List<User> users = userDao.findAll();
    return users.stream().map(user -> new UserResponsePublicDto(user.getId(), user.getUsername()))
      .collect(Collectors.toList());
  }

  public Optional<UserResponsePublicDto> getUserById(Long userId) {
    Optional<User> foundUser = userDao.findById(userId);
    if (foundUser.isPresent()) {
      return Optional.of(
        new UserResponsePublicDto(foundUser.get().getId(), foundUser.get().getUsername()));
    } else {
      return Optional.empty();
    }
  }
}
