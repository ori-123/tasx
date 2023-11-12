package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime deadline;

  @ManyToOne
  @JoinColumn(name = "project_owner_id")
  private User projectOwner;

  @ManyToMany
  @JoinTable(name = "project_assigned_employees",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> assignedEmployees;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "project")
  private List<Task> tasks;
}
