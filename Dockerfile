# Java 17 버전이 포함된 Docker Image 사용
FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

# 컨테이너가 실행될 때 기본적으로 실행될 명령어 지정 (Java Application 실행)
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "/app.jar"]