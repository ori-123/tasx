package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePrivateDto;
import com.codecool.tasx.controller.dto.user.UserUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.company.project.task.TaskService;
import com.codecool.tasx.service.converter.UserConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final UserConverter userConverter;
    private final CustomAccessControlService accessControlService;
    private final Logger logger;
    private final TaskService taskService;

    public UserService(UserDao userDao, UserConverter userConverter,
                       CustomAccessControlService accessControlService, Logger logger,
                       UserProvider userProvider, TaskService taskService) {
        this.userDao = userDao;
        this.userConverter = userConverter;
        this.accessControlService = accessControlService;
        this.userProvider = userProvider;
        this.taskService = taskService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Transactional
    public Optional<UserResponsePrivateDto> getUserById(Long userId)
            throws UnauthorizedException {
        Optional<User> foundUser = userDao.findById(userId);
        if (foundUser.isEmpty()) {
            logger.error("User with ID " + userId + " was not found");
            return Optional.empty();
        }
        User user = foundUser.get();
        return Optional.of(userConverter.getUserResponsePrivateDto(user));
    }

    @Transactional(rollbackOn = Exception.class)
    public UserResponsePrivateDto updateUser(UserUpdateRequestDto updateRequestDto)
            throws ConstraintViolationException {

        User user = userDao.findOneByEmail(updateRequestDto.email()).get();

        user.setScore(updateRequestDto.points());
        user.setEmail(updateRequestDto.email());
        user.setPassword(updateRequestDto.password());
        user.setUsername(updateRequestDto.username());

        User updatedUser = userDao.save(user);
        return userConverter.getUserResponsePrivateDto(user);
    }

}
