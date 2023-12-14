package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJoinRequestDao extends JpaRepository<ProjectJoinRequest,Long> {
    List<ProjectJoinRequest> findByProjectAndStatus(Project project, RequestStatus status);

  Optional<ProjectJoinRequest> findOneByProjectAndUser(Project project, User user);

    List<ProjectJoinRequest> findByUser(User user);
}
