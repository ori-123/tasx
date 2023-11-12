package com.codecool.tasx.model.company.reward;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.company.project.task.Task;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Reward {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToMany
  @JoinTable(name = "reward_tasks",
    joinColumns = @JoinColumn(name = "reward_id"),
    inverseJoinColumns = @JoinColumn(name = "task_id"))
  private List<Task> tasks;
}
