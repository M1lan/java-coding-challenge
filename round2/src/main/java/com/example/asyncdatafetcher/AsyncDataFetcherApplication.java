package com.example.asyncdatafetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.asyncdatafetcher")
public class AsyncDataFetcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsyncDataFetcherApplication.class, args);
    }
}
