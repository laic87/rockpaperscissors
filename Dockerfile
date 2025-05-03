# Start with a base Java mage
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]