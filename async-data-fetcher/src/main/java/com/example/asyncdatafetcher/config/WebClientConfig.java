package com.example.asyncdatafetcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/** Configuration class for setting up a WebClient bean. */
@Configuration
public class WebClientConfig {

  /**
   * Creates and configures a new instance of {@link WebClient}.
   *
   * @return a configured WebClient instance
   */
  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }
}
