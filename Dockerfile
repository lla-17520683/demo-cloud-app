# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /build
COPY gradle/ gradle/
COPY gradlew build.gradle ./
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew dependencies --no-daemon || true
COPY src/ src/
RUN ./gradlew clean bootJar --no-daemon -x test

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/build/libs/*.jar app.jar
COPY agent/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "/app/app.jar"]