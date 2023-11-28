package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.requests.ProjectJoinRequest;
import com.codecool.tasx.model.requests.RequestStatus;
import com.codecool.tasx.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Project p WHERE :user MEMBER OF p.employees")
    List<Project> findAllWithEmployee(@Param("user") User user);

    @Query("SELECT p FROM Project p WHERE :user NOT MEMBER OF p.employees AND p.id NOT IN " +
            "(SELECT pr.project.id FROM ProjectJoinRequest pr " +
            "WHERE pr.user = :user AND pr.status IN (:statuses))")
    List<Project> findAllWithoutEmployeeAndJoinRequest(
            @Param("user") User user,
            @Param("statuses") List<RequestStatus> statuses);
}
