package com.codecool.tasx.model.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyJoinRequestDao extends JpaRepository<CompanyJoinRequest,Long> {
}
