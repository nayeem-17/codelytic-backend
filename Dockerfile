# Use the official Maven image as the build stage
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and download dependencies to cache the layer
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project to the container
COPY . .

# Build the project using Maven
RUN mvn package -DskipTests

# Create a new stage with the AdoptOpenJDK 17 base image for the final production container
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file from the build stage to the final production container
COPY --from=build /app/target/codelytic-0.0.jar ./app.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8000

# Set the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
