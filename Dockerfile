# First stage: Use an OpenJDK 19 image to build the app
FROM openjdk:19-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y wget tar && \
    wget https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz && \
    tar -xzf apache-maven-3.8.6-bin.tar.gz && \
    mv apache-maven-3.8.6 /usr/local/apache-maven && \
    ln -s /usr/local/apache-maven/bin/mvn /usr/bin/mvn

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
