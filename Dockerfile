# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the project’s JAR file into the container at /app
COPY target/my-spring-boot-app-0.0.1-SNAPSHOT.jar /app/my-spring-boot-app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "bb-pets-adoption-0.0.1-SNAPSHOT.jar"]
