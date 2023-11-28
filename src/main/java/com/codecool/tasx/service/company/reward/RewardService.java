package com.codecool.tasx.service.company.reward;

import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.reward.RewardDao;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.RewardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardService {
    private final RewardDao rewardDao;
    private final CompanyDao companyDao;
    private final ProjectDao projectDao;
    private final RewardConverter rewardConverter;
    private final UserProvider userProvider;
    private final CustomAccessControlService accessControlService;
    private final Logger logger;

    @Autowired
    public RewardService(RewardDao rewardDao, CompanyDao companyDao, ProjectDao projectDao,
                         RewardConverter rewardConverter, UserProvider userProvider,
                         CustomAccessControlService accessControlService) {
        this.rewardDao = rewardDao;
        this.companyDao = companyDao;
        this.projectDao = projectDao;
        this.rewardConverter = rewardConverter;
        this.userProvider = userProvider;
        this.accessControlService = accessControlService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    // TODO: methods for CRUD operations
}
