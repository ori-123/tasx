package com.codecool.tasx.service.request;

import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.ProjectJoinRequestUpdateDto;
import com.codecool.tasx.exception.project.ProjectJoinRequestNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.exception.project.DuplicateProjectJoinRequestException;
import com.codecool.tasx.exception.project.UserAlreadyInProjectException;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.requests.ProjectJoinRequest;
import com.codecool.tasx.model.requests.ProjectJoinRequestDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.ProjectConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectRequestService {
    private final ProjectDao projectDao;
    private final ProjectJoinRequestDao requestDao;
    private final UserProvider userProvider;
    private final CustomAccessControlService accessControlService;
    private final ProjectConverter projectConverter;
    private final Logger logger;

    @Autowired
    public ProjectRequestService(
            ProjectDao projectDao, ProjectJoinRequestDao requestDao, UserProvider userProvider,
            CustomAccessControlService accessControlService, ProjectConverter projectConverter) {
        this.projectDao = projectDao;
        this.requestDao = requestDao;
        this.userProvider = userProvider;
        this.accessControlService = accessControlService;
        this.projectConverter = projectConverter;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Transactional
    public ProjectJoinRequestResponseDto createJoinRequest(Long projectId) {
        User user = userProvider.getAuthenticatedUser();
        Project project = projectDao.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(projectId));
        if (project.getAssignedEmployees().contains(user)) {
            throw new UserAlreadyInProjectException();
        }
        Optional<ProjectJoinRequest> duplicateRequest = requestDao.findOneByProjectAndUser(
                project,
                user);
        if (duplicateRequest.isPresent()) {
            throw new DuplicateProjectJoinRequestException();
        }
        ProjectJoinRequest savedRequest = requestDao.save(new ProjectJoinRequest(project, user));
        return projectConverter.getProjectJoinRequestResponseDto(savedRequest);
    }

    @Transactional
    public List<ProjectJoinRequestResponseDto> getJoinRequestsOfProject(Long projectId) {
        User user = userProvider.getAuthenticatedUser();
        Project project = projectDao.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(projectId));
        accessControlService.verifyProjectOwnerAccess(project, user);
        List<ProjectJoinRequest> requests = requestDao.findByProjectAndStatus(
                project,
                RequestStatus.PENDING);
        return projectConverter.getProjectJoinRequestResponseDtos(requests);
    }

    @Transactional
    public List<ProjectJoinRequestResponseDto> getJoinRequestsOfUser() {
        User user = userProvider.getAuthenticatedUser();
        List<ProjectJoinRequest> requests = requestDao.findByUser(user);
        return projectConverter.getProjectJoinRequestResponseDtos(requests);
    }

    @Transactional(rollbackOn = Exception.class)
    public void handleJoinRequest(
            Long projectId, Long requestId, ProjectJoinRequestUpdateDto updateDto) {
        ProjectJoinRequest request = requestDao.findById(requestId).orElseThrow(
                () -> new ProjectJoinRequestNotFoundException(requestId));
        User user = userProvider.getAuthenticatedUser();
        Project project = projectDao.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(projectId));
        accessControlService.verifyProjectOwnerAccess(project, user);
        request.setStatus(updateDto.requestStatus());
        if (request.getStatus().equals(RequestStatus.APPROVED)) {
            addUserToProject(request.getUser(), request.getProject());
            requestDao.delete(request);
        }
    }

    private void addUserToProject(User user, Project project) {
        project.assignEmployee(user);
    }
}
