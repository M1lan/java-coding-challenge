package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableCaching
public class DataFetchServiceCachingTest {

  @Autowired
  private DataFetchService dataFetchService;

  @Autowired
  private CacheManager cacheManager;

  @Test
  void testCaching() {
    // Call the service twice; the second call should be served from the cache.
    MergedData mergedData1 = dataFetchService.fetchMergedData().block();
    MergedData mergedData2 = dataFetchService.fetchMergedData().block();

    assertNotNull(mergedData1);
    assertNotNull(mergedData2);

    // Retrieve the cached value.
    // Since fetchMergedData takes no parameters, the default key is SimpleKey.EMPTY.
    Object cachedValue = cacheManager.getCache("mergedData").get(SimpleKey.EMPTY).get();
    assertNotNull(cachedValue);

    // The cached value is stored as a MergedData, so cast directly.
    MergedData cachedMergedData = (MergedData) cachedValue;
    assertNotNull(cachedMergedData);
  }
}
