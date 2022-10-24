FROM gradle:7.4-jdk17-alpine as builder
WORKDIR /build

# 그래들 파일이 변경 되었을 경우에만 새롭게 의존패키지를 다운로드 받음
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 이미지에서 애플리케이션 빌드
COPY . /build
CMD ls
RUN gradle clean build -x test

# APP
FROM openjdk:17.0-slim
WORKDIR /app

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /build/build/libs/backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "backend-0.0.1-SNAPSHOT.jar"              \
]