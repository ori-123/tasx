package com.codecool.tasx.model.company.project.task;

import jakarta.persistence.*;

@Entity
public class Expense {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private double price;
  private boolean paid;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;
}