package com.codecool.tasx.service.request;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
  private final CompanyDao companyDao;
  private final CompanyJoinRequestDao requestDao;
  private final UserDao userDao;
  private final CompanyConverter companyConverter;
  private final UserConverter userConverter;
  private final Logger logger;

  @Autowired
  public RequestService(
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

    List<CompanyJoinRequest> requests = requestDao.findByCompanyAndStatus(company,
      RequestStatus.PENDING);
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
      requestDao.delete(request);
    }
  }

  private void addUserToCompany(User user, Company company) {
    company.addEmployee(user);
  }
}
