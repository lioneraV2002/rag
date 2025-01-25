FROM openjdk:11-jdk-slim

# Copy your application JAR file
COPY target/ragserver-1.0-SNAPSHOT-shaded.jar /app/ragserver.jar

# Set the working directory
WORKDIR /app

# Install curl to load the model
RUN apt-get update && apt-get install -y curl

# Expose the ports
EXPOSE 11434
EXPOSE 8080
EXPOSE 6333

# Set the entry point to start the Java application and load the model
RUN curl http://ollama:11434/api/pull -d '{\"model\": \"llama3.2:1b\"}' &
CMD ["java", "-jar", "ragserver.jar"]