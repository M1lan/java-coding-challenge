/* ========== File: ./all_relevant_java_code.java ========== */



/* ========== File: ./pom.xml ========== */
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.2</version>
    <relativePath/>
  </parent>
  <groupId>com.example</groupId>
  <artifactId>async-data-fetcher</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <properties>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
	<resilience4j.version>2.3.0</resilience4j.version>
	<mockito-version>5.14.2</mockito-version>
  </properties>
  <dependencies>
	<dependency>
	  <groupId>io.projectreactor</groupId>
	  <artifactId>reactor-test</artifactId>
	  <scope>test</scope>
	</dependency>
	<dependency>
	  <groupId>org.springframework.boot</groupId>
	  <artifactId>spring-boot-starter-logging</artifactId>
	</dependency>
	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>


	<dependency>
      <groupId>io.github.resilience4j</groupId>
      <artifactId>resilience4j-bom</artifactId>
      <version>${resilience4j.version}</version>
      <type>pom</type>
      <scope>test</scope>
    </dependency>

	<dependency>
      <groupId>io.github.resilience4j</groupId>
      <artifactId>resilience4j-spring-boot3</artifactId>
      <version>${resilience4j.version}</version>
	</dependency>

	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
	</dependency>

	<dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>${mockito.version}</version>
	</dependency>

	<dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-junit-jupiter</artifactId>
	  <scope>test</scope>
	</dependency>
	<dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
	</dependency>
	<dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <scope>test</scope>
	</dependency>
	<dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
    </dependency>


	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>org.junit.vintage</groupId>
          <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
      </exclusions>
	</dependency>

	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>
  </dependencies>
  <build>
	<plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-help-plugin</artifactId>
        <version>3.3.0</version>
      </plugin>
	  <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <mainClass>com.example.asyncdatafetcher.AsyncDataFetcherApplication</mainClass>
        </configuration>
      </plugin>
      <plugin>
        <groupId>com.diffplug.spotless</groupId>
        <artifactId>spotless-maven-plugin</artifactId>
        <version>2.28.0</version>
        <configuration>
          <java>
            <includes>
              <include>src/main/java/**/*.java</include>
            </includes>
            <googleJavaFormat>
              <version>1.15.0</version>
            </googleJavaFormat>
          </java>
        </configuration>
        <executions>
          <execution>
            <id>spotless-check</id>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
          <execution>
            <id>spotless-apply</id>
            <phase>validate</phase>
            <goals>
              <goal>apply</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.15.0</version>
        <configuration>
          <failOnViolation>false</failOnViolation>
        </configuration>
        <executions>
          <execution>
            <phase>verify</phase>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>com.github.spotbugs</groupId>
        <artifactId>spotbugs-maven-plugin</artifactId>
        <version>4.7.3.0</version>
        <configuration>
          <failOnError>false</failOnError>
        </configuration>
        <executions>
          <execution>
            <phase>verify</phase>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.6.0</version>
        <executions>
          <execution>
            <phase>verify</phase>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <configLocation>google_checks.xml</configLocation>
          <charset>UTF-8</charset>
          <consoleOutput>true</consoleOutput>
          <failsOnError>true</failsOnError>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.8</version>
        <executions>
          <execution>
            <id>prepare-agent</id>
            <goals>
              <goal>prepare-agent</goal>
            </goals>
          </execution>
          <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals>
              <goal>report</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>



/* ========== File: ./target/classes/application.properties ========== */
api.posts-url=http://jsonplaceholder.typicode.com/posts?userId=1
api.user-url=http://jsonplaceholder.typicode.com/users/1

logging.level.com.example.asyncdatafetcher.service.DataFetchService=warn

management.endpoint.circuitbreakers.enabled=true
management.endpoints.web.exposure.include=health,info,circuitbreakers

resilience4j.circuitbreaker.instances.dataFetchService.failureRateThreshold=50
resilience4j.circuitbreaker.instances.dataFetchService.registerHealthIndicator=true

resilience4j.circuitbreaker.instances.default.registerHealthIndicator=true

resilience4j.circuitbreaker.instances.dataFetchService.slidingWindowSize=10

resilience4j.retry.instances.dataFetchService.maxAttempts=3
resilience4j.retry.instances.dataFetchService.waitDuration=500ms

spring.application.name=async-data-fetcher
spring.cache.type=simple



/* ========== File: ./src/main/resources/application.properties ========== */
api.posts-url=http://jsonplaceholder.typicode.com/posts?userId=1
api.user-url=http://jsonplaceholder.typicode.com/users/1

logging.level.com.example.asyncdatafetcher.service.DataFetchService=warn

management.endpoint.circuitbreakers.enabled=true
management.endpoints.web.exposure.include=health,info,circuitbreakers

resilience4j.circuitbreaker.instances.dataFetchService.failureRateThreshold=50
resilience4j.circuitbreaker.instances.dataFetchService.registerHealthIndicator=true

resilience4j.circuitbreaker.instances.default.registerHealthIndicator=true

resilience4j.circuitbreaker.instances.dataFetchService.slidingWindowSize=10

resilience4j.retry.instances.dataFetchService.maxAttempts=3
resilience4j.retry.instances.dataFetchService.waitDuration=500ms

spring.application.name=async-data-fetcher
spring.cache.type=simple



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/cli/CliRunner.java ========== */
package com.example.asyncdatafetcher.cli;

import com.example.asyncdatafetcher.service.DataFetchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {
  private final DataFetchService s;
  private final ObjectMapper om;

  /** Constructs a new CliRunner with the specified DataFetchService and ObjectMapper. */
  public CliRunner(DataFetchService s, ObjectMapper om) {
    this.s = s;
    this.om = om;
  }

  /** Executes the command line runner; fetches merged data if "cli" argument is provided. */
  public void run(String... args) throws Exception {
    if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
      var m = s.fetchMergedData().block();
      System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(m));
      System.exit(0);
    }
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/AsyncDataFetcherApplication.java ========== */
package com.example.asyncdatafetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AsyncDataFetcherApplication {
  /** entrypoint */
  public static void main(String[] args) {
    SpringApplication.run(AsyncDataFetcherApplication.class, args);
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/exception/GlobalExceptionHandler.java ========== */
package com.example.asyncdatafetcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
/**
 * TODO: Auto-generated Javadoc
 */
  public Mono<ResponseEntity<String>> handleException(Exception ex) {
    return Mono.just(
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error: " + ex.getMessage()));
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/config/WebClientConfig.java ========== */
package com.example.asyncdatafetcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
/**
 * TODO: Auto-generated Javadoc
 */
  public WebClient webClient() {
    // Customize the WebClient builder here as needed (timeouts, interceptors, etc.)
    return WebClient.builder().build();
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/model/User.java ========== */
package com.example.asyncdatafetcher.model;

public class User {
  private Long id;
  private String name;
  private String username;
  private String email;

  /** Constructs a new User instance. */
  public User() {}

  /** Returns the user's id. */
  public Long getId() {
    return id;
  }

  /** Sets the user's id. */
  public void setId(Long id) {
    this.id = id;
  }

  /** Returns the user's name. */
  public String getName() {
    return name;
  }

  /** Sets the user's name. */
  public void setName(String name) {
    this.name = name;
  }

  /** Returns the user's username. */
  public String getUsername() {
    return username;
  }

  /** Sets the user's username. */
  public void setUsername(String username) {
    this.username = username;
  }

  /** Returns the user's email. */
  public String getEmail() {
    return email;
  }

  /** Sets the user's email. */
  public void setEmail(String email) {
    this.email = email;
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/model/Post.java ========== */
package com.example.asyncdatafetcher.model;

public class Post {
  private Long userId;
  private Long id;
  private String title;
  private String body;

  /** Constructs a new Post instance. */
  public Post() {}

  /** Returns the post's userId. */
  public Long getUserId() {
    return userId;
  }

  /** Sets the post's userId. */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /** Returns the post's id. */
  public Long getId() {
    return id;
  }

  /** Sets the post's id. */
  public void setId(Long id) {
    this.id = id;
  }

  /** Returns the post's title. */
  public String getTitle() {
    return title;
  }

  /** Sets the post's title. */
  public void setTitle(String title) {
    this.title = title;
  }

  /** Returns the post's body. */
  public String getBody() {
    return body;
  }

  /** Sets the post's body. */
  public void setBody(String body) {
    this.body = body;
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/model/MergedData.java ========== */
package com.example.asyncdatafetcher.model;

import java.util.List;

public class MergedData {
  private User user;
  private List<Post> posts;

  /** Constructs a new MergedData instance with the given user and posts. */
  public MergedData(User user, List<Post> posts) {
    this.user = user;
    this.posts = posts;
  }

  /** Returns the user part of the merged data. */
  public User getUser() {
    return user;
  }

  /** Sets the user part of the merged data. */
  public void setUser(User user) {
    this.user = user;
  }

  /** Returns the list of posts. */
  public List<Post> getPosts() {
    return posts;
  }

  /** Sets the list of posts. */
  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/controller/DataFetchController.java ========== */
package com.example.asyncdatafetcher.controller;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.service.DataFetchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DataFetchController {
  private final DataFetchService s;

  /** Constructs a new DataFetchController with the given DataFetchService. */
  public DataFetchController(DataFetchService s) {
    this.s = s;
  }

  @GetMapping("/")
  /** Fetches and returns the merged data as a Mono. */
  public Mono<MergedData> get() {
    return s.fetchMergedData();
  }
}



/* ========== File: ./src/main/java/com/example/asyncdatafetcher/service/DataFetchService.java ========== */
package com.example.asyncdatafetcher.service;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.model.Post;
import com.example.asyncdatafetcher.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DataFetchService {

  private static final Logger log = LoggerFactory.getLogger(DataFetchService.class);
  private final WebClient wc;

  @Value("${api.user-url}")
  private String userUrl;

  @Value("${api.posts-url}")
  private String postsUrl;
/**
 * TODO: Auto-generated Javadoc
 */

  public DataFetchService(WebClient wc) {
    this.wc = wc;
  }

  @Cacheable("mergedData")
/**
 * TODO: Auto-generated Javadoc
 */
  public Mono<MergedData> fetchMergedData() {
    var userMono =
        wc.get()
            .uri(userUrl)
            .retrieve()
            .onStatus(
                status -> status.isError(),
                clientResponse -> Mono.error(new RuntimeException("User API failed")))
            .bodyToMono(User.class);

    var postsFlux =
        wc.get()
            .uri(postsUrl)
            .retrieve()
            .onStatus(
                status -> status.isError(),
                clientResponse -> Mono.error(new RuntimeException("Posts API failed")))
            .bodyToFlux(Post.class)
            .collectList();

    return Mono.zip(userMono, postsFlux, MergedData::new)
        .onErrorResume(
            e -> {
              log.warn("Error fetching data: {}", e.getMessage()); // disable Java stack traces!
              return Mono.empty();
            });
  }
}



/* ========== File: ./src/test/java/com/example/asyncdatafetcher/DataFetchIntegrationTest.java ========== */
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
                    // Assertions on the mergedData
                    // Example:
                    // assertNotNull(mergedData.getUser());
                    // assertFalse(mergedData.getPosts().isEmpty());
                });
    }
}



/* ========== File: ./src/test/java/com/example/asyncdatafetcher/service/DataFetchServiceUnitTest.java ========== */
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



/* ========== File: ./src/test/java/com/example/asyncdatafetcher/service/DataFetchServiceTest.java ========== */
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



/* ========== File: ./src/test/java/com/example/asyncdatafetcher/service/DataFetchServiceCachingTest.java ========== */
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
