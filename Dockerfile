FROM openjdk:17-slim AS builder

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Run Maven to build the application
RUN mvn clean package

# Copy WebDriverManager dependency (adjust version as needed)
COPY --from=builder target/lib/WebDriverManager-5.4.1.jar app/lib/WebDriverManager.jar

# Create a slimmer image for runtime
FROM openjdk:17-slim

WORKDIR /app

# Copy the jar file and WebDriverManager dependency from the builder stage
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder app/lib/WebDriverManager.jar app/lib/WebDriverManager.jar

# Set the classpath to include WebDriverManager
ENV CLASSPATH app/lib/WebDriverManager.jar:$CLASSPATH

# Command to run the application
CMD ["java", "-jar", "app.jar"]