# Package service
FROM maven:3.8.4-eclipse-temurin-17 as maven
WORKDIR /app
RUN mkdir src
COPY ./src ./src
COPY pom.xml .
RUN mvn package -DskipTests

# Run service
FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=maven /app .
# COPY ./target/backend-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]