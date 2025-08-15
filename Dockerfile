# ---- Step 1: Build stage ----
FROM gradle:8.9-jdk17 AS builder
WORKDIR /app

# Copy Gradle files first (better caching)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Warm up Gradle dependencies cache (no source code yet, so ignore failures)
RUN gradle build -x test || true

# Copy source code
COPY src ./src

# Build the jar (skip tests for speed)
RUN gradle clean bootJar -x test

# ---- Step 2: Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
