FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src
RUN chmod +x ./mvnw && ./mvnw -Dmaven.repo.local=/root/.m2/repository clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y curl netcat && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
