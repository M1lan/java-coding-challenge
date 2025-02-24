package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DataFetchServiceTest {

    @Autowired
    private DataFetchService dataFetchService;

    @Test
    void testFetchMergedData() {
        MergedData data = dataFetchService.fetchMergedData();
        assertNotNull(data, "merged data should not be null");
        assertNotNull(data.getUser(), "user should not be null");
        assertNotNull(data.getPosts(), "posts should not be null");
    }
}
