FROM openjdk:11
COPY target/ragserver-1.0-SNAPSHOT.jar /app/ragserver.jar
WORKDIR /app
CMD ["java", "-jar", "ragserver.jar"]
