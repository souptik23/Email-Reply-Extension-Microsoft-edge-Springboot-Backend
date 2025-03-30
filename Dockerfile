# Build stage using Maven with OpenJDK 21
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the Spring Boot application without running tests
RUN mvn clean package -DskipTests

# Run stage using minimal OpenJDK 21 runtime
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/email-writer-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
