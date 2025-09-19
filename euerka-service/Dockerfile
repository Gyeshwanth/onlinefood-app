FROM eclipse-temurin:17-jre-alpine


WORKDIR /app
COPY target/Eureka-Service.jar /app/Eureka-Service.jar

LABEL maintainer="Yeshwanth"

EXPOSE 8761

ENTRYPOINT ["java", "-jar","Eureka-Service.jar"]