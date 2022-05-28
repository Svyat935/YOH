# syntax=docker/dockerfile:experimental
####### Package service #######
FROM maven:3.8.4-eclipse-temurin-17 as maven
WORKDIR /app
COPY pom.xml .
RUN mkdir src
COPY ./src ./src
RUN --mount=type=cache,target=/root/.mv2 mvn clean package -DskipTests

# Run service
FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=maven /app .
# COPY ./target/backend-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]