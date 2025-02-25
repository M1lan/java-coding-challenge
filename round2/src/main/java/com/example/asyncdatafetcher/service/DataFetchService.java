package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.model.Post;
import com.example.asyncdatafetcher.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataFetchService {

    @Value("${api.user-url}")
    private String userUrl;

    @Value("${api.posts-url}")
    private String postsUrl;

    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataFetchService(ApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    public MergedData fetchMergedData() {
        try {
            String userJson = apiClient.get(userUrl);
            User user = objectMapper.readValue(userJson, User.class);

            String postsJson = apiClient.get(postsUrl);
            List<Post> posts = objectMapper.readValue(postsJson, new TypeReference<List<Post>>() {});

            return new MergedData(user, posts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
