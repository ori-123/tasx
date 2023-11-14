package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao extends JpaRepository<Project,Long> {
}
