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

@Service
public class DataFetchService {

  private static final Logger log = LoggerFactory.getLogger(DataFetchService.class);
  private final WebClient wc;

  @Value("${api.user-url}")
  private String userUrl;

  @Value("${api.posts-url}")
  private String postsUrl;

  /**
   * Constructs a new {@code DataFetchService} with the provided WebClient.
   *
   * @param wc the {@link WebClient} used to perform HTTP requests.
   */
  public DataFetchService(WebClient wc) {
    this.wc = wc;
  }

  /**
   * Fetches and merges user and post data from external APIs.
   *
   * @return a {@link Mono} that emits a {@link MergedData} object containing the merged user and
   *         posts data, or an empty {@code Mono} if an error occurs during data retrieval
   */
  @Cacheable("mergedData")
  public Mono<MergedData> fetchMergedData() {
    var userMono =
        wc.get()
            .uri(userUrl)
            .retrieve()
            .onStatus(
                status -> status.isError(),
                clientResponse -> Mono.error(new RuntimeException("User API failed")))
            .bodyToMono(User.class);

    var postsFlux =
        wc.get()
            .uri(postsUrl)
            .retrieve()
            .onStatus(
                status -> status.isError(),
                clientResponse -> Mono.error(new RuntimeException("Posts API failed")))
            .bodyToFlux(Post.class)
            .collectList();

    return Mono.zip(userMono, postsFlux, MergedData::new)
        .onErrorResume(
            e -> {
              log.warn("Error fetching data: {}", e.getMessage()); // disable Java stack traces!
              return Mono.empty();
            });
  }
}
