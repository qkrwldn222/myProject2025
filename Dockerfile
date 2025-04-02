FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/reservation.jar /app/reservation.jar

CMD ["java", "-jar", "/app/reservation.jar"]