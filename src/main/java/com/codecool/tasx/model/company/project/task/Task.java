package com.codecool.tasx.model.company.project.task;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private Importance importance;
  private int difficulty;
  private LocalDateTime startDate;
  private LocalDateTime deadline;
  private TaskStatus taskStatus;

  @ManyToOne
  @JoinColumn(name = "task_owner_id")
  private User taskOwner;

  @ManyToMany
  @JoinTable(name = "task_assigned_employees",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> assignedEmployees;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @OneToMany(mappedBy = "task")
  private List<Expense> expenses;
}
