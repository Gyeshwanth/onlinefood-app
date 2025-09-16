FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Create uploads directory
RUN mkdir -p /app/uploads

# Copy the JAR file
COPY target/product-service.jar /app/product-service.jar

# Create volume mount point
VOLUME ["/app/uploads"]

# Expose port
EXPOSE 7410


# Run the application
ENTRYPOINT ["java", "-jar", "product-service.jar"]
