FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/group-pt-member-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} group-pt-member.jar

ENTRYPOINT [ "java", "-Duser.timezone=Asia/Seoul", "-jar", "/group-pt-member.jar" ]