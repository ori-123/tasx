package com.codecool.tasx.service.company.reward;

import com.codecool.tasx.controller.dto.company.reward.RewardCreateRequestDto;
import com.codecool.tasx.controller.dto.company.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.company.reward.RewardUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.reward.RewardNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.company.reward.Reward;
import com.codecool.tasx.model.company.reward.RewardDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.CustomAccessControlService;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.RewardConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardService {
  private final RewardDao rewardDao;
  private final CompanyDao companyDao;
  private final RewardConverter rewardConverter;
  private final UserProvider userProvider;
  private final CustomAccessControlService accessControlService;
  private final Logger logger;

  @Autowired
  public RewardService(
    RewardDao rewardDao, CompanyDao companyDao, RewardConverter rewardConverter,
    UserProvider userProvider, CustomAccessControlService accessControlService) {
    this.rewardDao = rewardDao;
    this.companyDao = companyDao;
    this.rewardConverter = rewardConverter;
    this.userProvider = userProvider;
    this.accessControlService = accessControlService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public List<RewardResponseDto> getAllRewards(Long companyId)
    throws CompanyNotFoundException, UnauthorizedException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(company, user);
    List<Reward> rewards = company.getRewards();
    return rewardConverter.getRewardResponseDtos(rewards);
  }

  @Transactional
  public RewardResponseDto getRewardById(Long companyId, Long rewardId)
    throws RewardNotFoundException, UnauthorizedException {
    Reward reward = rewardDao.findByIdAndCompanyId(companyId, rewardId).orElseThrow(
      () -> new RewardNotFoundException(rewardId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyEmployeeAccess(reward.getCompany(), user);
    return rewardConverter.getRewardResponseDto(reward);
  }

  @Transactional(rollbackOn = Exception.class)
  public RewardResponseDto createReward(RewardCreateRequestDto createRequestDto, Long companyId)
    throws ConstraintViolationException, UnauthorizedException {
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));
    User user = userProvider.getAuthenticatedUser();
    accessControlService.verifyCompanyOwnerAccess(company, user);
    Reward reward = new Reward(createRequestDto.id(), createRequestDto.name(),
      createRequestDto.description(), company, createRequestDto.pointCost());
    rewardDao.save(reward);
    return rewardConverter.getRewardResponseDto(reward);
  }

  @Transactional(rollbackOn = Exception.class)
  public RewardResponseDto updateReward(
    RewardUpdateRequestDto updateRequestDto, Long companyId, Long rewardId)
    throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    Reward reward = rewardDao.findByIdAndCompanyId(companyId, rewardId).orElseThrow(
      () -> new RewardNotFoundException(rewardId));
    accessControlService.verifyCompanyOwnerAccess(reward.getCompany(), user);
    reward.setName(updateRequestDto.name());
    reward.setDescription(updateRequestDto.description());
    reward.setPointCost(updateRequestDto.pointCost());
    Reward savedReward = rewardDao.save(reward);
    return rewardConverter.getRewardResponseDto(savedReward);
  }

  @Transactional(rollbackOn = Exception.class)
  public void deleteReward(Long rewardId, Long companyId) {
    User user = userProvider.getAuthenticatedUser();
    Reward reward = rewardDao.findByIdAndCompanyId(companyId, rewardId).orElseThrow(
      () -> new RewardNotFoundException(rewardId));
    accessControlService.verifyCompanyOwnerAccess(reward.getCompany(), user);
    rewardDao.delete(reward);
  }

}
