package com.codecool.tasx.model.company;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.reward.Reward;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;

  @ManyToOne
  @JoinColumn(name = "company_owner_id")
  private User companyOwner;

  @ManyToMany
  @JoinTable(name = "company_employees", joinColumns = @JoinColumn(name = "company_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> employees;

  @OneToMany(mappedBy = "company")
  private List<Project> projects;

  @OneToMany(mappedBy = "company")
  private List<Reward> rewards;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public User getCompanyOwner() {
    return companyOwner;
  }

  public List<User> getEmployees() {
    return List.copyOf(employees);
  }

  public List<Project> getProjects() {
    return List.copyOf(projects);
  }

  public List<Reward> getRewards() {
    return List.copyOf(rewards);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Company company = (Company) object;
    return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(
      description, company.description) && Objects.equals(companyOwner, company.companyOwner) &&
      Objects.equals(employees, company.employees) && Objects.equals(projects, company.projects) &&
      Objects.equals(rewards, company.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, companyOwner, employees, projects, rewards);
  }

  @Override
  public String toString() {
    return "Company{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", companyOwner=" + companyOwner +
      ", employees=" + employees +
      ", projects=" + projects +
      ", rewards=" + rewards +
      '}';
  }
}