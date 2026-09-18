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

# Install Application Insights Java Agent
ARG AI_AGENT_VERSION=3.7.9
ARG AI_AGENT_SHA256=4ab7a442bf9defc7475d026c6b49042793ebb010c391a1db91ae07da0c04d848
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/${AI_AGENT_VERSION}/applicationinsights-agent-${AI_AGENT_VERSION}.jar applicationinsights-agent.jar
RUN echo "${AI_AGENT_SHA256}  applicationinsights-agent.jar" | sha256sum -c -

ENV OTEL_INSTRUMENTATION_LOGBACK_MDC_ENABLED="true"
ENV LOGGING_STRUCTURED_FORMAT_CONSOLE="logstash"
ENV OTEL_SERVICE_NAME="service-a"

EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/app/applicationinsights-agent.jar", "-jar", "/app/app.jar"]