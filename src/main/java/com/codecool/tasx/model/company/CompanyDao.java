package com.codecool.tasx.model.company;

import com.codecool.tasx.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDao extends JpaRepository<Company,Long> {
}
