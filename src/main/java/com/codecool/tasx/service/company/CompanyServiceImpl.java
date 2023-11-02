package com.codecool.tasx.service.company;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.CompanyDao;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl
  implements CompanyService {
  private final CompanyDao companyDao;
  private final UserDao userDao;

  //TODO:logging, error handling

  @Autowired
  public CompanyServiceImpl(CompanyDao companyDao, UserDao userDao) {
    this.companyDao = companyDao;
    this.userDao = userDao;
    createMockUsers();
  }

  //TODO:create actual users
  private void createMockUsers() {
    User user1 = new User();
    user1.setUsername("Mock User 1");
    userDao.save(user1);
    User user2 = new User();
    user1.setUsername("Mock User 2");
    userDao.save(user2);
    User user3 = new User();
    user1.setUsername("Mock User 3");
    userDao.save(user3);
  }

  @Override
  public List<CompanyResponsePublicDTO> getAll() {
    return companyDao.findAll().stream().map(
      company -> new CompanyResponsePublicDTO(company.getId(), company.getName(),
        company.getDescription())).collect(Collectors.toList());
  }

  @Override
  public Optional<CompanyResponsePrivateDTO> getById(Long companyId) {
    Optional<Company> company = companyDao.findById(companyId);
    if (company.isPresent()) {
      User owner = company.get().getOwner();
      return Optional.of(
        new CompanyResponsePrivateDTO(company.get().getId(), company.get().getName(),
          company.get().getDescription(),
          new UserResponsePublicDto(owner.getId(), owner.getUsername())));
    }
    return Optional.empty();
  }

  @Override
  public UserResponsePublicDto getOwnerByCompanyId(Long companyId) {
    Optional<Company> company = companyDao.findById(companyId);
    if (company.isEmpty()) {
      throw new CompanyNotFoundException(companyId);
    }
    User owner = company.get().getOwner();
    return new UserResponsePublicDto(owner.getId(), owner.getUsername());
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public CompanyResponsePrivateDTO create(Long ownerId, CompanyCreateRequestDto companyDetails)
    throws UserNotFoundException {
    Company company = new Company();
    Optional<User> owner = userDao.findById(ownerId);
    if (owner.isEmpty()) {
      throw new UserNotFoundException(ownerId);
    }
    company.setName(companyDetails.name());
    company.setDescription(companyDetails.description());
    company.setOwner(owner.get());
    Company createdCompany = companyDao.save(company);

    return new CompanyResponsePrivateDTO(createdCompany.getId(), createdCompany.getName(),
      createdCompany.getDescription(), new UserResponsePublicDto(
      createdCompany.getOwner().getId(),
      createdCompany.getOwner().getUsername()));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public CompanyResponsePrivateDTO update(
    Long companyId, Long ownerId, CompanyUpdateRequestDto companyDetails)
    throws CompanyNotFoundException, UnauthorizedException {
    Optional<Company> company = companyDao.findById(companyId);
    if (company.isEmpty()) {
      throw new CompanyNotFoundException(companyId);
    }
    if (company.get().getOwner().getId().equals(ownerId)) {
      //TODO: use spring security thing for this if there is one
      throw new UnauthorizedException("User is not the owner of this company");
    }
    Company foundCompany = company.get();
    foundCompany.setName(companyDetails.name());
    foundCompany.setDescription(companyDetails.description());
    Company updatedCompany = companyDao.save(foundCompany);

    return new CompanyResponsePrivateDTO(updatedCompany.getId(), updatedCompany.getName(),
      updatedCompany.getDescription(), new UserResponsePublicDto(
      updatedCompany.getOwner().getId(),
      updatedCompany.getOwner().getUsername()));
  }

  @Override
  public void delete(Long companyId, Long userId) throws UnauthorizedException {
    UserResponsePublicDto owner = getOwnerByCompanyId(userId);
    if (owner.userId().equals(userId)) {
      throw new UnauthorizedException();
    }
    companyDao.deleteById(companyId);
  }
}
