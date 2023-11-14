package com.codecool.tasx.model.company.project;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
  @JoinTable(name = "project_assigned_employees", joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> assignedEmployees;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "project")
  private List<Task> tasks;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }

  public User getProjectOwner() {
    return projectOwner;
  }

  public List<User> getAssignedEmployees() {
    return List.copyOf(assignedEmployees);
  }

  public Company getCompany() {
    return company;
  }

  public List<Task> getTasks() {
    return List.copyOf(tasks);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Project project = (Project) object;
    return Objects.equals(id, project.id) && Objects.equals(name, project.name) &&
      Objects.equals(description, project.description) && Objects.equals(
      startDate, project.startDate) && Objects.equals(deadline, project.deadline) &&
      Objects.equals(projectOwner, project.projectOwner) && Objects.equals(
      assignedEmployees, project.assignedEmployees) && Objects.equals(
      company, project.company) && Objects.equals(tasks, project.tasks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, startDate, deadline, projectOwner, assignedEmployees,
      company, tasks);
  }

  @Override
  public String toString() {
    return "Project{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", startDate=" + startDate +
      ", deadline=" + deadline +
      ", projectOwner=" + projectOwner +
      ", assignedEmployees=" + assignedEmployees +
      ", company=" + company +
      ", tasks=" + tasks +
      '}';
  }
}
