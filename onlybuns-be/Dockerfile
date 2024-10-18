# Use the official Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy only the pom.xml file to install dependencies first
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk-slim

# Copy the built JAR file from the build stage
COPY --from=build /app/target/onlybuns-0.0.1-SNAPSHOT.jar onlybuns.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "onlybuns.jar"]
