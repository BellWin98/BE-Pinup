# Java 17 버전이 포함된 Docker Image 사용
FROM azul/zulu-openjdk-alpine:17 AS builder

WORKDIR /app

COPY . .

# /app/build/libs/*.jar 파일을 아래 명령어를 통해 생성
RUN ./gradlew bootJar

# 새로운 work stage 시작, 기존 스테이지는 자동으로 사라진다.
FROM azul/zulu-openjdk-alpine:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar pinup.jar

# .jar 실행 시 환경변수(프로퍼티) 설정
ENV SPRING_PROFILE local

VOLUME /tmp

# 컨테이너가 실행될 때 기본적으로 실행될 명령어 지정 (Java Application 실행)
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "pinup.jar"]