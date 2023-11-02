package com.codecool.tasx.model.company;

import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Company {
  @Id
  @SequenceGenerator(name = "company_id_sequence", sequenceName = "company_id_sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_sequence")
  private Long id;

  @Column(unique = true)
  private String name;
  @Column
  private String description;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private List<User> employees;

  public Company() {
    this.employees = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<User> getEmployees() {
    return List.copyOf(employees);
  }

  public void addEmployee(User employee) {
    this.employees.add(employee);
  }

  public void removeEmployee(User employee) {
    this.employees.remove(employee);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    return Objects.equals(id, company.id) && Objects.equals(name, company.name) &&
      Objects.equals(description, company.description) && Objects.equals(
      owner, company.owner) && Objects.equals(employees, company.employees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, owner, employees);
  }

  @Override
  public String toString() {
    return "Company{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", owner=" + owner +
      '}';
  }
}
