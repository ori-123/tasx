package com.codecool.tasx.service.company;

import com.codecool.tasx.controller.dto.company.CompanyCreateRequestDto;
import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.company.CompanyUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
  List<CompanyResponsePublicDTO> getAll();

  Optional<CompanyResponsePrivateDTO> getById(Long companyId);

  UserResponsePublicDto getOwnerByCompanyId(Long companyId);

  CompanyResponsePrivateDTO create(Long ownerId, CompanyCreateRequestDto companyDetails);

  CompanyResponsePrivateDTO update(
    Long companyId, Long ownerId, CompanyUpdateRequestDto companyDetails)
    throws CompanyNotFoundException, UnauthorizedException;

  void delete(Long companyId, Long userId);
}
