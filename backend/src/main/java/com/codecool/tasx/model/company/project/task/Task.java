package com.codecool.tasx.model.company.project.task;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  @JoinTable(name = "task_assigned_employees", joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> assignedEmployees;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @OneToMany(mappedBy = "task")
  private List<Expense> expenses;

  public Task() {
  }

  public Task(
    String name, String description, Importance importance, int difficulty,
    LocalDateTime startDate, LocalDateTime deadline, TaskStatus taskStatus, User taskOwner,
    Project project) {
    this.name = name;
    this.description = description;
    this.importance = importance;
    this.difficulty = difficulty;
    this.startDate = startDate;
    this.deadline = deadline;
    this.taskStatus = taskStatus;
    this.taskOwner = taskOwner;
    this.project = project;
    this.assignedEmployees = new ArrayList<>();
    this.expenses = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Importance getImportance() {
    return importance;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }

  public TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public User getTaskOwner() {
    return taskOwner;
  }

  public List<User> getAssignedEmployees() {
    return List.copyOf(assignedEmployees);
  }

  public Project getProject() {
    return project;
  }

  public List<Expense> getExpenses() {
    return List.copyOf(expenses);
  }

  public void assignEmployee(User user) {
    assignedEmployees.add(user);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setImportance(Importance importance) {
    this.importance = importance;
  }

  public void setDifficulty(int difficulty) {
    this.difficulty = difficulty;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public void setDeadline(LocalDateTime deadline) {
    this.deadline = deadline;
  }

  public void setTaskStatus(TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
  }

  public void setTaskOwner(User taskOwner) {
    this.taskOwner = taskOwner;
  }

  public void setAssignedEmployees(List<User> assignedEmployees) {
    this.assignedEmployees = assignedEmployees;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void setExpenses(List<Expense> expenses) {
    this.expenses = expenses;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Task task = (Task) object;
    return difficulty == task.difficulty && Objects.equals(id, task.id) &&
      Objects.equals(name, task.name) && Objects.equals(
      description, task.description) && importance == task.importance && Objects.equals(
      startDate, task.startDate) && Objects.equals(deadline, task.deadline) &&
      taskStatus == task.taskStatus && Objects.equals(taskOwner, task.taskOwner) &&
      Objects.equals(assignedEmployees, task.assignedEmployees) && Objects.equals(
      project, task.project) && Objects.equals(expenses, task.expenses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, importance, difficulty, startDate, deadline,
      taskStatus, taskOwner, assignedEmployees, project, expenses);
  }

  @Override
  public String toString() {
    return "Task{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", importance=" + importance +
      ", difficulty=" + difficulty +
      ", startDate=" + startDate +
      ", deadline=" + deadline +
      ", taskStatus=" + taskStatus +
      ", taskOwner=" + taskOwner +
      ", assignedEmployees=" + assignedEmployees +
      ", project=" + project +
      ", expenses=" + expenses +
      '}';
  }
}
