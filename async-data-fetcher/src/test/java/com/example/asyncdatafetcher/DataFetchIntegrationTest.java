package com.example.asyncdatafetcher;

import com.example.asyncdatafetcher.model.MergedData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataFetchIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void fetchData() {
        webTestClient.get().uri("/")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(MergedData.class)
			.consumeWith(response -> {
                    MergedData mergedData = response.getResponseBody();
                });
    }
}
