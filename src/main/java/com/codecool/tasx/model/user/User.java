package com.codecool.tasx.model.user;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "user_account")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;
  private String password;
  private Set<Role> roles;

  @OneToMany(mappedBy = "companyOwner")
  private List<Company> ownedCompanies;

  @ManyToMany(mappedBy = "employees")
  private List<Company> companies;

  @OneToMany(mappedBy = "projectOwner")
  private List<Project> ownedProjects;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Set<Role> getRoles() {
    return Set.copyOf(roles);
  }

  public List<Company> getOwnedCompanies() {
    return List.copyOf(ownedCompanies);
  }

  public List<Company> getCompanies() {
    return List.copyOf(companies);
  }

  public List<Project> getOwnedProjects() {
    return List.copyOf(ownedProjects);
  }

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles=new HashSet<>();
    roles.add(Role.USER);
    this.ownedCompanies=new ArrayList<>();
    this.companies=new ArrayList<>();
    this.ownedProjects=new ArrayList<>();

  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    User user = (User) object;
    return Objects.equals(id, user.id) && Objects.equals(
      username, user.username) && Objects.equals(email, user.email) &&
      Objects.equals(password, user.password) && Objects.equals(
      roles, user.roles) && Objects.equals(ownedCompanies, user.ownedCompanies) &&
      Objects.equals(companies, user.companies) && Objects.equals(
      ownedProjects, user.ownedProjects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, password, roles, ownedCompanies, companies,
      ownedProjects);
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", email='" + email + '\'' +
      ", roles=" + roles +
      '}';
  }
}

