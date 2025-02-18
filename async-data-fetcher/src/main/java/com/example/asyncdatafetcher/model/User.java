package com.example.asyncdatafetcher.model;

public class User {
  private Long id;
  private String name;
  private String username;
  private String email;

  /** Constructs a new User instance. */
  public User() {}

  /** Returns the user's id. */
  public Long getId() {
    return id;
  }

  /** Sets the user's id. */
  public void setId(Long id) {
    this.id = id;
  }

  /** Returns the user's name. */
  public String getName() {
    return name;
  }

  /** Sets the user's name. */
  public void setName(String name) {
    this.name = name;
  }

  /** Returns the user's username. */
  public String getUsername() {
    return username;
  }

  /** Sets the user's username. */
  public void setUsername(String username) {
    this.username = username;
  }

  /** Returns the user's email. */
  public String getEmail() {
    return email;
  }

  /** Sets the user's email. */
  public void setEmail(String email) {
    this.email = email;
  }
}
