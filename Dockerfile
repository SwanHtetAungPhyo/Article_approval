# Build Stage
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the application JAR file from the build stage
COPY --from=build /app/target/Article_Approval-0.0.1.jar app.jar

# Expose the application port
EXPOSE 8080

# Set environment variables (optional, but recommended for consistency)
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
