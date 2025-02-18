package com.example.asyncdatafetcher.model;

/*  EXAMPLE JSON from "user" endpoint
  {
  "user": {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }
*/


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
