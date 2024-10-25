# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Cache Maven dependencies by copying only the pom.xml first
COPY pom.xml ./

# Pre-download dependencies to use the cache in future builds
RUN mvn dependency:go-offline

# Copy the rest of the project source code
COPY src ./src

# Package the application, skipping the tests for faster builds
RUN mvn clean package -DskipTests

# Stage 2: Run the application using Maven and enable hot-reload
FROM maven:3.8.5-openjdk-17

# Set the working directory
WORKDIR /app

# Copy the application source code from the build stage for hot-reloading
COPY --from=build /app /app

# Mount a volume for Maven to cache dependencies during development
VOLUME /root/.m2

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Command to run the application in development mode with hot-reloading
CMD ["mvn", "spring-boot:run"]
