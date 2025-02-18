package com.example.asyncdatafetcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DataFetchController {

    @GetMapping("/data")
    public Mono<String> fetchData() {

        // TODO: Add async fetching/merging of JSON from source API.

        return Mono.just("{\"message\": \"Hello, world!\"}");
    }
}
