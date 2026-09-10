# Build stage
FROM gradle:9.7.1-jdk21 AS build
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew build.gradle ./
RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew \
    && ./gradlew dependencies --no-daemon || true

COPY src/ src/
RUN ./gradlew clean bootJar --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
