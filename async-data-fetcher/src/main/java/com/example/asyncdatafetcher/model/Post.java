package com.example.asyncdatafetcher.model;

public class Post {
  private Long userId;
  private Long id;
  private String title;
  private String body;

  /** Constructs a new Post instance. */
  public Post() {}

  /** Returns the post's userId. */
  public Long getUserId() {
    return userId;
  }

  /** Sets the post's userId. */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /** Returns the post's id. */
  public Long getId() {
    return id;
  }

  /** Sets the post's id. */
  public void setId(Long id) {
    this.id = id;
  }

  /** Returns the post's title. */
  public String getTitle() {
    return title;
  }

  /** Sets the post's title. */
  public void setTitle(String title) {
    this.title = title;
  }

  /** Returns the post's body. */
  public String getBody() {
    return body;
  }

  /** Sets the post's body. */
  public void setBody(String body) {
    this.body = body;
  }
}
