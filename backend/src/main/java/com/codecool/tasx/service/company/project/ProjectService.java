package com.codecool.tasx.service.company.project;

import com.codecool.tasx.controller.dto.project.ProjectCreateRequestDto;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.ProjectConverter;
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
  private final CompanyDao companyDao;
  private final ProjectConverter projectConverter;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public ProjectService(
    ProjectDao projectDao, CompanyDao companyDao, ProjectConverter projectConverter,
    UserProvider userProvider, CustomAccessControlService accessControlService) {
    this.projectDao = projectDao;
    this.companyDao = companyDao;
    this.accessControlService = accessControlService;
    this.projectConverter = projectConverter;
    this.userProvider = userProvider;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<ProjectResponsePublicDTO> getProjectsWithoutUser(Long companyId)
    throws UnauthorizedException {
    User user = userProvider.getAuthenticatedUser();
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    List<Project> projects = projectDao.findAllWithoutEmployeeAndJoinRequestAndCompany(
      user, List.of(RequestStatus.PENDING, RequestStatus.DECLINED), company);
    return projectConverter.getProjectResponsePublicDtos(projects);
  }

  @Transactional
  public List<ProjectResponsePublicDTO> getProjectsWithUser(Long companyId)
    throws UnauthorizedException {
    User user = userProvider.getAuthenticatedUser();
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    List<Project> projects = projectDao.findAllWithEmployeeAndCompany(user, company);
    return projectConverter.getProjectResponsePublicDtos(projects);
  }

  @Transactional
  public Optional<ProjectResponsePrivateDTO> getProjectById(Long projectId)
    throws UnauthorizedException {
    Optional<Project> foundProject = projectDao.findById(projectId);
    if (foundProject.isEmpty()) {
      logger.error("Project with ID " + projectId + " was not found");
      return Optional.empty();
    }
    Project project = foundProject.get();
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyAssignedToProjectAccess(foundProject.get(), user);
    return Optional.of(projectConverter.getProjectResponsePrivateDto(project));
  }

  @Transactional(rollbackOn = Exception.class)
  public ProjectResponsePrivateDTO createProject(
    ProjectCreateRequestDto createRequestDto, Long companyId) throws ConstraintViolationException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    Project project = new Project(createRequestDto.name(), createRequestDto.description(),
      createRequestDto.startDate(), createRequestDto.deadline(), user, company);
    project.assignEmployee(user);
    projectDao.save(project);
    return projectConverter.getProjectResponsePrivateDto(project);
  }

  @Transactional(rollbackOn = Exception.class)
  public ProjectResponsePrivateDTO updateProject(
    ProjectUpdateRequestDto updateRequestDto, Long projectId) throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Project project = projectDao.findById(projectId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    accessControlService.verifyAssignedToProjectAccess(project, user);
    project.setName(updateRequestDto.name());
    project.setDescription(updateRequestDto.description());
    project.setStartDate(updateRequestDto.startDate());
    project.setDeadline(updateRequestDto.deadline());
    Project savedProject = projectDao.save(project);
    return projectConverter.getProjectResponsePrivateDto(savedProject);
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteProject(Long projectId) {
    User user = userProvider.getAuthenticatedUser();
    Project project = projectDao.findById(projectId).orElseThrow(
      () -> new ProjectNotFoundException(projectId));
    accessControlService.verifyProjectOwnerAccess(project, user);
    projectDao.deleteById(projectId);
  }
}
