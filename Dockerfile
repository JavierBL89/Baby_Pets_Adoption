# Use an official Maven image to build the app
FROM maven:3.8.1-openjdk-19 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official Java runtime as a parent image
FROM openjdk:19-jre-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/bb-pets-adoption-0.0.1-SNAPSHOT.jar /app/bb-pets-adoption-0.0.1-SNAPSHOT.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "bb-pets-adoption-0.0.1-SNAPSHOT.jar"]