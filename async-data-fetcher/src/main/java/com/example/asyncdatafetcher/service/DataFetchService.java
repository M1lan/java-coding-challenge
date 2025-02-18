package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.model.Post;
import com.example.asyncdatafetcher.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DataFetchService {

  private static final Logger log = LoggerFactory.getLogger(DataFetchService.class);
  private final WebClient wc;

  @Value("${api.user-url}")
  private String userUrl;

  @Value("${api.posts-url}")
  private String postsUrl;

  public DataFetchService(WebClient wc) {
    this.wc = wc;
  }

  @Cacheable("mergedData")
  public Mono<MergedData> fetchMergedData() {
    Mono<User> userMono = wc.get()
      .uri(userUrl)
      .retrieve()
      .bodyToMono(User.class);

    Mono<List<Post>> postsMono = wc.get()
      .uri(postsUrl)
      .retrieve()
      .bodyToFlux(Post.class)
      .collectList();

    return Mono.zip(userMono, postsMono, MergedData::new)
      .onErrorResume(e -> {
          log.warn("Error fetching data: {}", e.getMessage());
          return Mono.empty();
        });
  }
}
