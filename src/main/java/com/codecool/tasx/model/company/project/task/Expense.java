package com.codecool.tasx.model.company.project.task;

import jakarta.persistence.*;

import java.util.Objects;

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

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }

  public boolean isPaid() {
    return paid;
  }

  public Task getTask() {
    return task;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Expense expense = (Expense) object;
    return Double.compare(price, expense.price) == 0 && paid == expense.paid &&
      Objects.equals(id, expense.id) && Objects.equals(name, expense.name) &&
      Objects.equals(task, expense.task);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, paid, task);
  }

  @Override
  public String toString() {
    return "Expense{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", price=" + price +
      ", paid=" + paid +
      ", task=" + task +
      '}';
  }
}