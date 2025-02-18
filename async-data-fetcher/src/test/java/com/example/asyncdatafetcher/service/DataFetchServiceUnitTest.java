package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.model.Post;
import com.example.asyncdatafetcher.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Demonstrates a fully compiling Mockito test for WebClient calls.
 * Notice that we use raw types (no generics) for RequestHeadersUriSpec,
 * RequestHeadersSpec, etc. to avoid the "cannot convert" compile errors
 * related to wildcard captures.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataFetchServiceUnitTest {

  @Mock
  private WebClient webClient;

  // Raw mocks to avoid generics capture issues:
  @Mock
  private WebClient.RequestHeadersUriSpec userUriSpec;   // for user GET
  @Mock
  private WebClient.RequestHeadersUriSpec postsUriSpec;  // for posts GET

  @Mock
  private WebClient.RequestHeadersSpec userHeadersSpec;
  @Mock
  private WebClient.RequestHeadersSpec postsHeadersSpec;

  @Mock
  private WebClient.ResponseSpec userResponseSpec;
  @Mock
  private WebClient.ResponseSpec postsResponseSpec;

  @InjectMocks
  private DataFetchService dataFetchService;

  @BeforeEach
  void init() {
    // Set the private fields in DataFetchService for the URLs:
    ReflectionTestUtils.setField(dataFetchService, "userUrl", "mockUserUrl");
    ReflectionTestUtils.setField(dataFetchService, "postsUrl", "mockPostsUrl");
  }

  @Test
  void fetchMergedData_success() {
    // Mock data
    User mockUser = new User();
    mockUser.setId(111L);
    mockUser.setName("Jane Doe");

    Post mockPost = new Post();
    mockPost.setId(222L);

    List<Post> mockPosts = List.of(mockPost);

    // 1) The first time we call webClient.get(), we want userUriSpec returned,
    //    the second time we call it, we want postsUriSpec returned.
    when(webClient.get())
      .thenReturn(userUriSpec)
      .thenReturn(postsUriSpec);

    // 2) For the user request:
    when(userUriSpec.uri(eq("mockUserUrl"))).thenReturn(userHeadersSpec);
    when(userHeadersSpec.retrieve()).thenReturn(userResponseSpec);
    when(userResponseSpec.onStatus(any(), any())).thenReturn(userResponseSpec);
    when(userResponseSpec.bodyToMono(User.class)).thenReturn(Mono.just(mockUser));

    // 3) For the posts request:
    when(postsUriSpec.uri(eq("mockPostsUrl"))).thenReturn(postsHeadersSpec);
    when(postsHeadersSpec.retrieve()).thenReturn(postsResponseSpec);
    when(postsResponseSpec.onStatus(any(), any())).thenReturn(postsResponseSpec);
    when(postsResponseSpec.bodyToFlux(Post.class)).thenReturn(Flux.fromIterable(mockPosts));

    // 4) Invoke the service
    Mono<MergedData> mergedDataMono = dataFetchService.fetchMergedData();

    // 5) Verify the results
    MergedData merged = mergedDataMono.block();
    assertNotNull(merged, "MergedData must not be null");
    assertNotNull(merged.getUser(), "User must not be null");
    assertEquals(111L, merged.getUser().getId());
    assertEquals("Jane Doe", merged.getUser().getName());

    assertNotNull(merged.getPosts(), "Posts list must not be null");
    assertEquals(1, merged.getPosts().size());
    assertEquals(222L, merged.getPosts().get(0).getId());
  }


  @Test
  void fetchMergedData_errorOnUserCall() {
    // The code under test does two gets: first for user, second for posts.
    // So let's stub them in order:
    when(webClient.get())
      .thenReturn(userUriSpec)   // the first "get()" call
      .thenReturn(postsUriSpec); // the second "get()" call

    // 1) User call stubbing: if code calls .uri("mockUserUrl"), return userHeadersSpec
    when(userUriSpec.uri("mockUserUrl")).thenReturn(userHeadersSpec);
    when(userHeadersSpec.retrieve()).thenReturn(userResponseSpec);
    when(userResponseSpec.onStatus(any(), any())).thenReturn(userResponseSpec);
    // Make it fail
    when(userResponseSpec.bodyToMono(User.class))
      .thenReturn(Mono.error(new RuntimeException("API Error")));

    // 2) Posts call stubbing: code might still attempt the call
    when(postsUriSpec.uri("mockPostsUrl")).thenReturn(postsHeadersSpec);
    when(postsHeadersSpec.retrieve()).thenReturn(postsResponseSpec);
    when(postsResponseSpec.onStatus(any(), any())).thenReturn(postsResponseSpec);
    when(postsResponseSpec.bodyToFlux(Post.class)).thenReturn(Flux.empty());

    // Now your test can verify that the user call fails

    StepVerifier.create(dataFetchService.fetchMergedData())
      .expectNextCount(0)  // no results
      .expectComplete()
      .verify();

  }

}
