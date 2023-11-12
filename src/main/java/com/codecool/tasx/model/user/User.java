package com.codecool.tasx.model.user;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;
  private String password;
  private Set<Role> roles;

  @OneToMany(mappedBy = "companyOwner")
  private List<Company> ownedCompanies;

  @ManyToMany(mappedBy = "employees")
  private List<Company> companies;

  @OneToMany(mappedBy = "projectOwner")
  private List<Project> ownedProjects;

  public User() {
  }
}

