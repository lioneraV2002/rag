FROM docker.arvancloud.ir/openjdk:23-slim

# Install curl to load the model
RUN apt-get update && \
    apt-get install --no-install-recommends --assume-yes curl && \
    rm -rf /var/lib/apt/lists/*

# Copy your application JAR file
COPY target/ragserver-1.0-SNAPSHOT.jar app/ragserver.jar


# Set the working directory
WORKDIR /app

# Expose the ports
EXPOSE 8080

# Set the entry point to start the Java application and load the model
CMD ["java", "-jar", "ragserver.jar"]
