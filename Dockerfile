# Use an official OpenJDK runtime as a parent image
FROM 522814707388.dkr.ecr.us-east-2.amazonaws.com/java-image:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/TafDatastoreService.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
