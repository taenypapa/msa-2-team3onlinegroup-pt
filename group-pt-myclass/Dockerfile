FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=group-pt-myclass/target/group-pt-myclass-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} group-pt-myclass.jar

ENTRYPOINT [ "java", "-Duser.timezone=Asia/Seoul", "-jar", "/group-pt-myclass.jar" ]