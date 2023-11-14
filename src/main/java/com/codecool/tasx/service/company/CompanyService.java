package com.codecool.tasx.service.company;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.converter.CompanyConverter;
import com.codecool.tasx.service.converter.ProjectConverter;
import com.codecool.tasx.service.converter.RewardConverter;
import com.codecool.tasx.service.converter.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
  private final CompanyDao companyDao;
  private final CompanyConverter companyConverter;
  private final UserConverter userConverter;
  private final Logger logger;

  @Autowired
  public CompanyService(
    CompanyDao companyDao, CompanyConverter companyConverter, UserConverter userConverter) {
    this.companyDao = companyDao;
    this.companyConverter = companyConverter;
    this.userConverter = userConverter;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public List<CompanyResponsePublicDTO> getAllCompanies() {
    List<Company> companies = companyDao.findAll();
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  public Optional<CompanyResponsePrivateDTO> getCompanyById(Long userId, Long companyId)
    throws UnauthorizedException {
    Optional<Company> foundCompany = companyDao.findById(companyId);
    if (foundCompany.isEmpty()) {
      logger.error("Company with ID " + companyId + " was not found");
      return Optional.empty();
    }
    Company company = foundCompany.get();

    if (!userConverter.getUserIds(company.getEmployees()).contains(userId)) {
      logger.error(
        "User with ID " + userId + " is not an employee of company with ID " + companyId);
      throw new UnauthorizedException();
    }

    return Optional.of(companyConverter.getCompanyResponsePrivateDto(company));
  }

  public void createCompany(CompanyCreateRequestDto createRequestDto){

  }
}
