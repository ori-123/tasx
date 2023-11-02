package com.codecool.tasx.model.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {
  @Id
  @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
  private Long id;

  @Column(unique = true)
  private String username;

  public User() {

  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      '}';
  }
}
