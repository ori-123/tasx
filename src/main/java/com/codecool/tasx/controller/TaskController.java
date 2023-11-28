package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.project.ProjectResponsePrivateDTO;
import com.codecool.tasx.controller.dto.task.TaskCreateRequestDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePrivateDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.task.TaskUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/projects/{projectId}/tasks")
public class TaskController {

}
