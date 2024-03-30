FROM openjdk:17-slim as builder

WORKDIR /app


# 빌드 컨텍스트에서 애플리케이션의 JAR 파일을 작업 디렉토리로 복사
# 복사할 JAR 파일의 이름과 위치를 정확히 지정해야 합니다. 예를 들어, build/libs/app.jar
COPY build/libs/mejai-gg-0.0.1-SNAPSHOT.jar mejai.jar


# JAR 파일을 실행 가능한 스프링 부트 애플리케이션으로 빌드


# JAR 파일 실행
ENTRYPOINT ["java","-jar", "mejai.jar"]
