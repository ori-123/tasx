package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyJoinRequestDao extends JpaRepository<CompanyJoinRequest,Long> {
  List<CompanyJoinRequest> findByCompany(Company company);
}
