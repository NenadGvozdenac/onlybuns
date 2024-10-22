# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml to download dependencies
COPY pom.xml .

# Pre-download dependencies without building the project to speed up future builds
RUN mvn dependency:go-offline

# Copy the rest of the project source code
COPY src ./src

# Package the application, skipping the tests for faster builds in dev environments
RUN mvn clean package -DskipTests

# Stage 2: Run the application using Maven and enable hot-reload
FROM maven:3.8.5-openjdk-17

# Set the working directory
WORKDIR /app

# Copy the application source code from the build stage for hot-reloading
COPY --from=build /app /app

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Command to run the application in development mode, using spring-boot:run for hot-reloading
CMD ["mvn", "spring-boot:run"]
