# Async Data Fetcher

A minimal Spring Boot microservice that asynchronously fetches data from
external APIs, merges it, and exposes the result as a REST API or CLI
output.  Built with Spring Boot WebFlux, it leverages caching,
asynchronous programming, and Docker for containerization.

## Features
- Asynchronous data fetching using Spring WebFlux and Reactor.
- Merged JSON output from multiple endpoints.
- Dual operation modes: REST API and CLI.
- Caching for improved performance.
- Docker and Docker Compose support.

## Prerequisites
- JDK 17+
- Maven (or use the provided Maven wrapper)
- Docker (for containerization)

## Getting Started

### Build
Using the Justfile:
```bash
just build
```

Or with Maven:
```bash
./mvnw clean package
```

### Run in Server Mode
```bash
just dev
```

Access the REST API at
[http://localhost:8080/](http://localhost:8080/).

### Run in CLI Mode

```bash
just cli
```

### Run Tests
```bash
just test
```

### Docker
Build the Docker image:
```bash
just docker-build
```

Run with Docker Compose:
```bash
just docker-up
```
## For Developers
- Use the Maven wrapper for consistency.
- Follow the established code style ("spotless").
- Leverage the Justfile for common tasks, use it for dev and pipelines.
- Run tests frequently using `just test`.
- Use Docker (and Docker Compose) for containerized environments.

## License
MIT License
