# Use Eclipse Temurin JDK 17 with Alpine for small image size
FROM amazoncorretto:21-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file
COPY target/account-management-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
