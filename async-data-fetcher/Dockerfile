FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY src ./src
COPY .mvn ./.mvn

RUN ./mvnw dependency:go-offline && ./mvnw package -DskipTests -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/async-data-fetcher-0.0.1-SNAPSHOT.jar /app/async-data-fetcher-0.0.1-SNAPSHOT.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "async-data-fetcher-0.0.1-SNAPSHOT.jar"]
