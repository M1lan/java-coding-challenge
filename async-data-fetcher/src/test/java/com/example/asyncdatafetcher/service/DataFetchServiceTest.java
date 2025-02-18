package com.example.asyncdatafetcher.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * A minimal Spring Boot "smoke" test that confirms the DataFetchService bean is loaded by the
 * application context. It does NOT invoke any external APIs.
 */
@SpringBootTest
class DataFetchServiceTest {

  @Autowired
  private DataFetchService dataFetchService;

  @Test
  void contextLoads() {
    // Just verify the service bean is present in the Spring context.
    assertNotNull(dataFetchService, "DataFetchService should be autowired and not null");
  }
}
