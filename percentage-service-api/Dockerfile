FROM amazoncorretto:21-alpine AS build

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar", "-Dspring.profiles.active=docker"]
