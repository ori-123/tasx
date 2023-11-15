package com.codecool.tasx.model.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {

  //https://www.baeldung.com/spring-data-jpa-query

  @Query("SELECT c FROM Company c WHERE :userId MEMBER OF c.employees")
  List<Company> findAllWithEmployeeId(@Param("userId") Long userId);

  @Query("SELECT c FROM Company c WHERE :userId NOT MEMBER OF c.employees")
  List<Company> findAllWithoutEmployeeId(@Param("userId") Long userId);
}
