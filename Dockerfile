# First stage: Use a JDK 19 image and install Maven
FROM maven:3.8.6-openjdk-18 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Second stage: Create the runtime image
FROM openjdk:19-jdk-hotspot

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/bb-pets-adoption-0.0.1-SNAPSHOT.jar /app/bb-pets-adoption-0.0.1-SNAPSHOT.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "bb-pets-adoption-0.0.1-SNAPSHOT.jar"]
