package com.codecool.tasx.model.company;

import com.codecool.tasx.model.company.project.Project;
import com.codecool.tasx.model.company.reward.Reward;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

import java.util.List;

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
  @JoinTable(name = "company_employees",
    joinColumns = @JoinColumn(name = "company_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> employees;

  @OneToMany(mappedBy = "company")
  private List<Project> projects;

  @OneToMany(mappedBy = "company")
  private List<Reward> rewards;
}