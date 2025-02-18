package com.example.asyncdatafetcher.model;

import java.util.List;

public class MergedData {
  private User user;
  private List<Post> posts;

/**
 * TODO: Auto-generated Javadoc
 */
  public MergedData(User user, List<Post> posts) {
    this.user = user;
    this.posts = posts;
  }

/**
 * TODO: Auto-generated Javadoc
 */
  public User getUser() {
    return user;
  }

/**
 * TODO: Auto-generated Javadoc
 */
  public void setUser(User user) {
    this.user = user;
  }

/**
 * TODO: Auto-generated Javadoc
 */
  public List<Post> getPosts() {
    return posts;
  }

/**
 * TODO: Auto-generated Javadoc
 */
  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}
