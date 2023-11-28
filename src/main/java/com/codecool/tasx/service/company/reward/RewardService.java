package com.codecool.tasx.service.company.reward;

import com.codecool.tasx.controller.dto.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.task.TaskResponsePublicDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.project.ProjectNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.project.ProjectDao;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.company.reward.Reward;
import com.codecool.tasx.model.company.reward.RewardDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.RewardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public List<RewardResponseDto> getAllRewards(Long companyId)
            throws UnauthorizedException {
        Company company = companyDao.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
        User user = userProvider.getAuthenticatedUser();
        accessControlService.verifyCompanyEmployeeAccess(company, user);
        List<Reward> rewards = company.getRewards();
        return rewardConverter.getRewardResponseDtos(rewards);
    }

    public Optional<RewardResponseDto> getRewardkById(Long rewardId, Long companyId) throws UnauthorizedException {
        Company company = companyDao.findById(companyId).orElseThrow(
                () -> new CompanyNotFoundException(companyId));
        Optional<Reward> foundReward = company.getRewards().stream().filter(
                reward -> Objects.equals(reward.getId(), rewardId)).findFirst();
        if (foundReward.isEmpty()) {
            logger.error("Rewrd with ID " + rewardId + " was not found");
            return Optional.empty();
        }
        Reward reward = foundReward.get();
        User user = userProvider.getAuthenticatedUser();
        accessControlService.verifyCompanyEmployeeAccess(company, user);
        return Optional.of(rewardConverter.getRewardResponseDto(reward));
    }
}
