package com.codecool.tasx.service.project;

import com.codecool.tasx.controller.dto.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.converter.ProjectConverter;
import com.codecool.tasx.service.converter.UserConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
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
    private final CompanyDao companyDao;
    private final ProjectConverter projectConverter;
    private final UserConverter userConverter;
    private final Logger logger;

    @Autowired
    public ProjectService(ProjectDao projectDao, UserDao userDao,
                          CompanyDao companyDao, ProjectConverter projectConverter, UserConverter userConverter) {
        this.projectDao = projectDao;
        this.userDao = userDao;
        this.companyDao = companyDao;
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

    @Transactional
    public ProjectResponsePrivateDTO createProject(ProjectCreateRequestDto createRequestDto, Long userId)
    throws ConstraintViolationException {

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Company company = companyDao.findById(createRequestDto.companyId())
                .orElseThrow(() -> new CompanyNotFoundException(createRequestDto.companyId()));

        Project savedProject = projectDao.save(
                new Project(
                        createRequestDto.name(),
                        createRequestDto.description(),
                        createRequestDto.startDate(),
                        createRequestDto.deadline(),
                        user,
                        company
                )
        );

        return projectConverter.getProjectResponsePrivateDto(savedProject);
    }

    @Transactional
    public ProjectResponsePrivateDTO updateProject(ProjectUpdateRequestDto updateRequestDto, Long userId, Long projectId)
            throws ConstraintViolationException {

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Project project = projectDao.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Project savedProject = projectDao.save(
                new Project(
                        updateRequestDto.name(),
                        updateRequestDto.description(),
                        updateRequestDto.startDate(),
                        updateRequestDto.deadline(),
                        user,
                        company
                )
        );

        return projectConverter.getProjectResponsePrivateDto(savedProject);
    }
}
