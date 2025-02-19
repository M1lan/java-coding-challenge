## Async Fetch-Merge-Serve Programming Challenge.

- see `./prototype_bash/` for the prototype implementation in Bash.
- see `./async-data-fetcher` for the spring boot implementation, as requested.
- see `./rust-wasm-coding-challenge` for the rust implementation  (and WASM deployment!)

Browse to https://test.milan-santosi.workers.dev for the merged JSON!
Or, do: `curl -sSL https://test.milan-santosi.workers.dev`


## Problem statement formulation

Dear [REDACTED] and dear reviewers from [REDACTED],

as per original Problem statement:

> I am creating a "Spring Boot application that demonstrates my
> expertise in modern Java Spring Boot development; time-boxed in 3
> hours".


## Plan
The application integrates data from multiple APIs asynchronously,
featuring a REST endpoint and CLI interface.  I will implement it using
Spring Boot, add Project Reactor for reactive programming, and also add
Spring WebClient for HTTP communications.  Key technical highlights
include asynchronous data processing, caching, and comprehensive error
handling.  The project showcases my experience with:

- Java/Spring Boot ecosystem
- Reactive programming
- RESTful API design
- Caching (built-into Spring Boot)
- Test-driven development

I'd be happy to discuss this project in more detail during an interview.

Best of regards,

Milan

## Updated Problem Statement
Develop a Spring Boot application that fetches user and post data from
two separate APIs, merges the data, and provides it through a REST
endpoint. The application should also support fetching and displaying
the merged data via a command-line interface (CLI).
### Requirements
#### Functional Requirements
1. Data Fetching:
   - Fetch user data from a specified API endpoint.
   - Fetch post data from a specified API endpoint.
   - Merge the fetched user and post data into a single response.
2. REST Endpoint:
   - Provide a REST endpoint (/) that returns the merged data in JSON.
3. CLI Support:
   - Implement a CLI that fetches and displays the merged data when the
     application is run with a specific argument (cli).
4. Error Handling:
   - Handle errors gracefully when fetching data from the APIs.
   - Log errors and return an empty response if data fetching fails.
5. Caching:
   - Implement caching for the merged data to improve performance.
#### Non-Functional Requirements
1. Performance:
   - The application should fetch and merge data asynchronously to
     ensure optimal performance.
2. Scalability:
   - The application should be designed to handle a large number of
     requests efficiently.
3. Maintainability:
   - The code should be well-structured, documented, and follow best
     practices for maintainability.
### Technology Stack
- Programming Language: Java
- Framework: Spring Boot
- Reactive Programming: Project Reactor (for asynchronous data fetching)
- HTTP Client: Spring WebClient
- Caching: Spring Cache Abstraction
- Testing: JUnit 5, Spring Boot Test, ...
- Build Tool: Maven (or Gradle(?))

### Initial Thoughts and Planning
- Approach:
  - Start by setting up a basic Spring Boot application using the
    provided helper script.
  - Implement the data fetching logic using Spring WebClient to fetch
    user and post data asynchronously.
  - Merge the fetched data into a single response object.
  - Create a REST controller to expose the merged data via a REST
    endpoint.
  - Implement a CLI runner to fetch and display the merged data when the
    application is run with the cli argument.
  - Add caching to the merged data to improve performance.
  - Write integration tests to ensure the application works as expected.
- Implementation Decisions:
  - Use Project Reactor for asynchronous data fetching to ensure the
    application remains responsive.
  - Implement error handling to log errors and return an empty response
    if data fetching fails.
  - Use Spring Cache Abstraction to cache the merged data and improve
    performance.
  - Write comprehensive tests to cover all functional requirements and
    edge cases.
