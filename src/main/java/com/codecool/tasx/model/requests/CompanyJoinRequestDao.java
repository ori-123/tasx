package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyJoinRequestDao extends JpaRepository<CompanyJoinRequest,Long> {
  List<CompanyJoinRequest> findByCompanyAndStatus(Company company, RequestStatus status);

  Optional<CompanyJoinRequest> findOneByCompanyAndUser(Company company, User user);

  List<CompanyJoinRequest> findByUser(User user);
}
