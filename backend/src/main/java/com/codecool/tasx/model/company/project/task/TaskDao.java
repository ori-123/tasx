package com.codecool.tasx.model.company.project.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskDao extends JpaRepository<Task, Long> {
  /*@Query(
    "SELECT t FROM Task t WHERE t.taskStatus NOT IN :finishedStatuses")
  Optional<Task> findByIdAndUnfinished(Long id, List<TaskStatus> finishedStatuses);*/
}
