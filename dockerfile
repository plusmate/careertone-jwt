FROM openjdk:17-jdk-slim

WORKDIR /app

COPY app.jar app.jar

ENV SPRING_PROFILES_ACTIVE=local

ENTRYPOINT ["java", "-jar", "app.jar"]