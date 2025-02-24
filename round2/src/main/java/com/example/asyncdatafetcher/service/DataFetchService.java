package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.model.Post;
import com.example.asyncdatafetcher.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class DataFetchService {

    @Value("${api.user-url}")
    private String userUrl;

    @Value("${api.posts-url}")
    private String postsUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DataFetchService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public MergedData fetchMergedData() {
        try {
            // fetch user data
            HttpRequest userRequest = HttpRequest.newBuilder()
				.uri(URI.create(userUrl))
				.GET()
				.build();
            HttpResponse<String> userResponse = httpClient.send(userRequest, HttpResponse.BodyHandlers.ofString());
            if (userResponse.statusCode() != 200) {
                throw new RuntimeException("user api failed with status " + userResponse.statusCode());
            }
            User user = objectMapper.readValue(userResponse.body(), User.class);

            // fetch posts data
            HttpRequest postsRequest = HttpRequest.newBuilder()
				.uri(URI.create(postsUrl))
				.GET()
				.build();
            HttpResponse<String> postsResponse = httpClient.send(postsRequest, HttpResponse.BodyHandlers.ofString());
            if (postsResponse.statusCode() != 200) {
                throw new RuntimeException("posts api failed with status " + postsResponse.statusCode());
            }
            List<Post> posts = objectMapper.readValue(postsResponse.body(), new TypeReference<List<Post>>(){});

            return new MergedData(user, posts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
