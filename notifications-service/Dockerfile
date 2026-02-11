# ---------- Stage 1: build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts ./
RUN chmod +x gradlew

COPY accounts-service accounts-service
COPY cash-service cash-service
COPY transfer-service transfer-service
COPY front-controller front-controller
COPY my-bank-commons my-bank-commons
COPY my-bank-api-errors my-bank-api-errors
COPY my-bank-service-clients my-bank-service-clients
COPY notifications-service notifications-service
COPY api-gateway api-gateway

RUN ./gradlew :notifications-service:bootJar --no-daemon

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/notifications-service/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
