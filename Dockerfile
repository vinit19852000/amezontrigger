FROM openjdk:17-slim AS builder

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Run Maven to build the application
RUN mvn clean package 

# ... (rest of your Dockerfile)
# Create a slimmer image for runtime
FROM openjdk:17-slim

WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]