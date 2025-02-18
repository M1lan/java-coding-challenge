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
/**
 * TODO: Auto-generated Javadoc
 */

  public User() {}
/**
 * TODO: Auto-generated Javadoc
 */

  public Long getId() {
    return id;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public void setId(Long id) {
    this.id = id;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public String getName() {
    return name;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public void setName(String name) {
    this.name = name;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public String getUsername() {
    return username;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public void setUsername(String username) {
    this.username = username;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public String getEmail() {
    return email;
  }
/**
 * TODO: Auto-generated Javadoc
 */

  public void setEmail(String email) {
    this.email = email;
  }
}
