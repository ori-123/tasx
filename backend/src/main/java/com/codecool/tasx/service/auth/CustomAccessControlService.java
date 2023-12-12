package com.codecool.tasx.service.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
import org.springframework.stereotype.Service;

/**
 * Handles parts of the access control that are based on the business logic of the application<br>
 * For example, it can verify, that user is the owner of a company, is assigned to a project, etc.
 */
@Service
public class CustomAccessControlService {
  /**
   * Verifies that {@link Company} is owned by {@link User}
   */
  public void verifyCompanyOwnerAccess(Company company, User user) throws UnauthorizedException {
    if (!company.getCompanyOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not the owner of company with ID " + company.getId());
    }
  }

  /**
   * Verifies that {@link User} is an employee of {@link Company}
   */
  public void verifyCompanyEmployeeAccess(Company company, User user) throws UnauthorizedException {
    if (!company.getEmployees().contains(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not employed by company with ID " + company.getId());
    }
  }

  /**
   * Verifies that {@link Project} is owned by {@link User}
   */
  public void verifyProjectOwnerAccess(Project project, User user) throws UnauthorizedException {
    if (!project.getProjectOwner().equals(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not the owner of project with ID " + project.getId());
    }
  }

  /**
   * Verifies that {@link User} is assigned to {@link Project}
   */
  public void verifyAssignedToProjectAccess(Project project, User user)
    throws UnauthorizedException {
    if (!project.getAssignedEmployees().contains(user)) {
      throw new UnauthorizedException(
        "User with ID " + user.getId() + " is not assigned to project with ID " + project.getId());
    }
  }
}
