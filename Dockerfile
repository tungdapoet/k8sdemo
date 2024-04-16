# Build stage with Maven and JDK 17
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY ./pom.xml ./

# Download all required dependencies into the image
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY ./src /app/src

# Build the project and create the jar file
RUN mvn clean package -DskipTests

# Run stage with JRE to reduce image size
FROM openjdk:17-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Expose port 8080 for the application
EXPOSE 8080

# Copy the jar file from the build stage to the run stage
COPY --from=build /app/target/*.jar /app/spring-boot-application.jar

# Command to run the application
CMD ["java", "-jar", "spring-boot-application.jar"]
