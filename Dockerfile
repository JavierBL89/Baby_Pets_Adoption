# First stage: Use a JDK 19 image and install Maven
FROM openjdk:19-slim AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://repos.apache.org/repos/keys/apache-maven-3.8.6-asc | apt-key add - && \
    echo "deb http://apache.org/repos/asf/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz" > /etc/apt/sources.list.d/maven.list && \
    apt-get update && \
    apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Second stage: Create the runtime image
FROM openjdk:19-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/bb-pets-adoption-0.0.1-SNAPSHOT.jar /app/bb-pets-adoption-0.0.1-SNAPSHOT.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "bb-pets-adoption-0.0.1-SNAPSHOT.jar"]
