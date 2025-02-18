# async-data-fetcher

This is a Spring Boot microservice that fetches third-party API data
asynchronously, merges the JSON responses into a unified blob, and
serves it via a REST API.

For your convenience, it also includes a CLI mode without the server.

## Getting Started

- Test the project using Maven (no build):
  ```
  ./mvnw test
  ```
- Build the project using Maven (no testing):
  ```
  ./mvnw clean package -DskipTests -Dmaven.test.skip=true
  ```

- Run the application:
  ```
  ./mvnw spring-boot:run
  # or alternatively:
  java -jar target/async-data-fetcher-0.0.1-SNAPSHOT.jar
  ```

- At a later point, I might include a Justfile (similar to Makefile),
  because the Maven cli is quite frankly, a little bit user-unfriendly.


## Endpoints

- GET `/`: Returns the unified JSON data.

Happy coding (to myself)!
Happy reviewing (to you)!
