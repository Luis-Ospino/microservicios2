# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Copy source and build application
COPY src src
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime (lightweight)
FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="microservicios@example.com"
LABEL description="Microservicio de Cuentas y Movimientos - Spring Boot 3"

# Security: Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Application setup
ARG APP_PORT=8082
ENV SERVER_PORT=${APP_PORT}
ENV JAVA_OPTS="-XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=UTC"

WORKDIR /app
RUN chown -R spring:spring /app

# Copy jar from build stage
COPY --from=build --chown=spring:spring /workspace/target/*.jar app.jar

# Switch to non-root user
USER spring

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher health || exit 1

# Expose configurable port
EXPOSE ${APP_PORT}

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$SERVER_PORT -jar /app/app.jar"]
