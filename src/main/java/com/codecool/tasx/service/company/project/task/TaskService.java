package com.codecool.tasx.service.company.project.task;

import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.project.task.TaskDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.TaskConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskDao taskDao;
    private final CompanyDao companyDao;
    private final ProjectDao projectDao;
    private final TaskConverter taskConverter;
    private final UserProvider userProvider;
    private final CustomAccessControlService accessControlService;
    private final Logger logger;

    @Autowired
    public TaskService(TaskDao taskDao, CompanyDao companyDao, ProjectDao projectDao,
                       TaskConverter taskConverter, UserProvider userProvider,
                       CustomAccessControlService accessControlService) {
        this.taskDao = taskDao;
        this.companyDao = companyDao;
        this.projectDao = projectDao;
        this.taskConverter = taskConverter;
        this.userProvider = userProvider;
        this.accessControlService = accessControlService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public List<TaskResponsePublicDto> getAllTasks(Long projectId, Long companyId) throws ProjectNotFoundException, UnauthorizedException {
        Company company = companyDao.findById(companyId).orElseThrow(
                () -> new CompanyNotFoundException(companyId));
        Project project = projectDao.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(projectId));
        User user = userProvider.getAuthenticatedUser();
        accessControlService.verifyCompanyEmployeeAccess(company, user);
        List<Task> tasks = project.getTasks();
        return taskConverter.getTaskResponsePublicDtos(tasks);
    }



}
