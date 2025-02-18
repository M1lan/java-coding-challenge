package com.example.asyncdatafetcher.model;

import java.util.List;

public class MergedData {
  private User user;
  private List<Post> posts;

  /** Constructs a new MergedData instance with the given user and posts. */
  public MergedData(User user, List<Post> posts) {
    this.user = user;
    this.posts = posts;
  }

  /** Returns the user part of the merged data. */
  public User getUser() {
    return user;
  }

  /** Sets the user part of the merged data. */
  public void setUser(User user) {
    this.user = user;
  }

  /** Returns the list of posts. */
  public List<Post> getPosts() {
    return posts;
  }

  /** Sets the list of posts. */
  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}
