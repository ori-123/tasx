package com.codecool.tasx.service.project;

import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.converter.ProjectConverter;
import com.codecool.tasx.service.converter.UserConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final ProjectConverter projectConverter;
    private final UserConverter userConverter;
    private final Logger logger;

    @Autowired
    public ProjectService(ProjectDao projectDao, UserDao userDao,
                          ProjectConverter projectConverter, UserConverter userConverter) {
        this.projectDao = projectDao;
        this.userDao = userDao;
        this.projectConverter = projectConverter;
        this.userConverter = userConverter;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public List<ProjectResponsePublicDTO> getAllProjects() {
        List<Project> projects = projectDao.findAll();
        return projectConverter.getProjectResponsePublicDtos(projects);
    }

    @Transactional
    public Optional<ProjectResponsePrivateDTO> getProjectById(Long userId, Long projectId) throws UnauthorizedException {
        Optional<Project> foundProject = projectDao.findById(projectId);
        if (foundProject.isEmpty()) {
            logger.error("Project with ID " + projectId + " was not found");
            return Optional.empty();
        }
        Project project = foundProject.get();

        if(!userConverter.getUserIds(project.getAssignedEmployees()).contains(userId) &&
            !userConverter.getUserIds(List.of(project.getProjectOwner())).contains(userId)) {
            logger.error("User with ID " + userId + " not assigned to project " + projectId);
            throw new UnauthorizedException();
        }

        return Optional.of(projectConverter.getProjectResponsePrivateDto(project));
    }
}
