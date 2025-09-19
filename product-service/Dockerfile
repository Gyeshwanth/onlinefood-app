FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app


# Copy the JAR file
COPY target/product-service.jar /app/product-service.jar


# Expose port
EXPOSE 7410


# Run the application
ENTRYPOINT ["java", "-jar", "product-service.jar"]
