package com.codecool.tasx.model.user;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.Project;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_account")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  @OneToMany(mappedBy = "companyOwner", fetch = FetchType.EAGER)
  private List<Company> ownedCompanies;

  @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
  private List<Company> companies;

  @OneToMany(mappedBy = "projectOwner", fetch = FetchType.EAGER)
  private List<Project> ownedProjects;

  @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.EAGER)
  private List<Project> projects;

  public Long getId() {
    return id;
  }

  /**
   * Returns the actual username, since getUsername is reserved for the subject, which is the email
   */
  public String getActualUsername() {
    return username;
  }

  /**
   * In {@link UserDetails} the subject is always named "username"<br>
   * In our application the subject is the email, but since {@link User} implements
   * {@link UserDetails}, there must be a method named "getUsername" which actually returns the
   * email...
   */
  @Override
  public String getUsername() {
    return email;
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

  public List<Project> getProjects() {
    return projects;
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
    this.roles = new HashSet<>();
    roles.add(Role.USER);
    this.ownedCompanies = new ArrayList<>();
    this.companies = new ArrayList<>();
    this.ownedProjects = new ArrayList<>();

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(
      Collectors.toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean isEnabled() {
    //TODO: impl
    return true;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof User user)) {
      return false;
    }
    return Objects.equals(id, user.id) && Objects.equals(
      username, user.username) && Objects.equals(email, user.email) &&
      Objects.equals(password, user.password) && Objects.equals(
      roles, user.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, password, roles);
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

