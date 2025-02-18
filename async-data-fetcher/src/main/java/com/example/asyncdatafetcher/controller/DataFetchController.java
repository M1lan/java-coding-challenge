package com.example.asyncdatafetcher.controller;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.service.DataFetchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DataFetchController {
  private final DataFetchService s;

  /** Construct a new DataFetchController with the given DataFetchService. */
  public DataFetchController(DataFetchService s) {
    this.s = s;
  }

  @GetMapping("/")
  /** Fetch and return the merged data as a "Mono". */
  public Mono<MergedData> get() {
    return s.fetchMergedData();
  }
}
