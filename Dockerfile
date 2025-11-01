FROM openjdk:17-jdk-slim

EXPOSE 5500

COPY target/MoneyTransferService-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]