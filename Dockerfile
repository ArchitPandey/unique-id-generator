FROM openjdk:17-jdk-alpine
LABEL authors="Archit"
WORKDIR /app
COPY target/unique-id-generator-0.0.1-SNAPSHOT.jar unique-id-generator-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/unique-id-generator-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080