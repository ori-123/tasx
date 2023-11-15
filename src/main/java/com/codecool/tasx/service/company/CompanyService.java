package com.codecool.tasx.service.company;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestResponseDto;
import com.codecool.tasx.controller.dto.requests.CompanyJoinRequestUpdateDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyJoinRequestNotFoundException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.requests.CompanyJoinRequest;
import com.codecool.tasx.model.requests.CompanyJoinRequestDao;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.converter.CompanyConverter;
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
public class CompanyService {
  private final CompanyDao companyDao;
  private final CompanyJoinRequestDao requestDao;
  private final UserDao userDao;
  private final CompanyConverter companyConverter;
  private final UserConverter userConverter;
  private final Logger logger;

  @Autowired
  public CompanyService(
    CompanyDao companyDao, CompanyJoinRequestDao requestDao, UserDao userDao,
    CompanyConverter companyConverter,
    UserConverter userConverter) {
    this.companyDao = companyDao;
    this.requestDao = requestDao;
    this.userDao = userDao;
    this.companyConverter = companyConverter;
    this.userConverter = userConverter;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public List<CompanyResponsePublicDTO> getAllCompanies() {
    List<Company> companies = companyDao.findAll();
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional
  public List<CompanyResponsePublicDTO> getCompaniesWithoutUserId(Long userId)
    throws UserNotFoundException {
    User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<Company> companies = companyDao.findAllWithoutEmployeeAndPendingRequest(user);
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional
  public List<CompanyResponsePublicDTO> getCompaniesWithUserId(Long userId)
    throws UserNotFoundException {
    User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<Company> companies = companyDao.findAllWithEmployee(user);
    return companyConverter.getCompanyResponsePublicDtos(companies);
  }

  @Transactional
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

  @Transactional
  public CompanyResponsePrivateDTO createCompany(
    CompanyCreateRequestDto createRequestDto,
    Long userId)
    throws ConstraintViolationException {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));
    Company savedCompany = companyDao.save(new Company(createRequestDto.name(),
      createRequestDto.description(), user));
    return companyConverter.getCompanyResponsePrivateDto(savedCompany);
  }

  @Transactional
  public CompanyResponsePrivateDTO updateCompany(
    CompanyUpdateRequestDto updateRequestDto, Long userId, Long companyId)
    throws ConstraintViolationException {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId)
    );
    if (!user.getOwnedCompanies().contains(company)) {
      throw new UnauthorizedException();
    }

    company.setName(updateRequestDto.name());
    company.setDescription(updateRequestDto.description());

    Company savedCompany = companyDao.save(company);
    return companyConverter.getCompanyResponsePrivateDto(savedCompany);
  }

  @Transactional
  public void deleteCompany(Long companyId, Long userId) {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));
    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId)
    );
    if (!user.getOwnedCompanies().contains(company)) {
      throw new UnauthorizedException();
    }
    companyDao.deleteById(companyId);
  }

  @Transactional
  public CompanyJoinRequestResponseDto createJoinRequest(
    Long userId, Long companyId) {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));

    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));

    if (userConverter.getUserIds(company.getEmployees()).contains(userId)) {
      throw new UserAlreadyInCompanyException();
    }

    Optional<CompanyJoinRequest> duplicateRequest = requestDao.findOneByCompanyAndUser(
      company,
      user);
    if (duplicateRequest.isPresent()) {
      throw new DuplicateCompanyJoinRequestException();
    }

    CompanyJoinRequest savedRequest = requestDao.save(new CompanyJoinRequest(company, user));

    return companyConverter.getCompanyJoinRequestResponseDto(savedRequest);
  }

  @Transactional
  public List<CompanyJoinRequestResponseDto> getJoinRequestsOfCompany(Long companyId, Long userId) {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));

    Company company = companyDao.findById(companyId).orElseThrow(
      () -> new CompanyNotFoundException(companyId));

    if (!user.getOwnedCompanies().contains(company)) {
      throw new UnauthorizedException();
    }

    List<CompanyJoinRequest> requests = requestDao.findByCompany(company);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }


  @Transactional
  public List<CompanyJoinRequestResponseDto> getJoinRequestsOfUser(Long userId) {
    User user =
      userDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId));

    List<CompanyJoinRequest> requests = requestDao.findByUser(user);
    return companyConverter.getCompanyJoinRequestResponseDtos(requests);
  }

  @Transactional(rollbackOn = Exception.class)
  public void handleJoinRequest(
    Long userId, Long requestId, CompanyJoinRequestUpdateDto updateDto) {
    CompanyJoinRequest request = requestDao.findById(requestId).orElseThrow(
      () -> new CompanyJoinRequestNotFoundException(requestId));

    User user = userDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));

    if (!user.getOwnedCompanies().contains(request.getCompany())) {
      throw new UnauthorizedException();
    }

    request.setStatus(updateDto.status());

    if (request.getStatus().equals(RequestStatus.APPROVED)) {
      addUserToCompany(request.getUser(), request.getCompany());
    }
    requestDao.delete(request);
  }

  private void addUserToCompany(User user, Company company) {
    company.addEmployee(user);
  }
}
