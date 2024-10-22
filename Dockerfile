# Stage 1: Build
FROM openjdk:17-slim AS builder

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml and the source code
COPY pom.xml ./ 
COPY src ./src

# Run Maven to build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-slim

WORKDIR /app

# Install required packages for Chrome
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    gnupg2 \
    libxi6 \
    libgconf-2-4 \
    libnss3 \
    libxss1 \
    libxrandr2 \
    libasound2 \
    fonts-liberation \
    libappindicator3-1 \
    xdg-utils \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/deb stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
