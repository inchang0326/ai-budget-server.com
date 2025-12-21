# Build stage
FROM gradle:8.7-jdk17-alpine AS builder
WORKDIR /workspace
COPY . .
RUN gradle :eureka-server:bootJar --no-daemon

# Run stage
FROM eclipse-temurin:17-jre
ENV PORT=8761
WORKDIR /app
COPY --from=builder /workspace/eureka-server/build/libs/*.jar app.jar
EXPOSE 8761
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]