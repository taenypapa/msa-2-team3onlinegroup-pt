FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} group-pt-reservation.jar

ENTRYPOINT [ "java", "-Duser.timezone=Asia/Seoul", "-jar", "/group-pt-reservation.jar" ]