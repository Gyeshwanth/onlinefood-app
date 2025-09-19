FROM eclipse-temurin:17-jre-alpine


WORKDIR /app
COPY target/Config-Server.jar  /app/Config-Server.jar

EXPOSE 1000

LABEL maintainer ="yeshwanth"

ENTRYPOINT ["java", "-jar", "Config-Server.jar"]