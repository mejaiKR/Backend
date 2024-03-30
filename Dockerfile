FROM openjdk:17-slim  AS builder
## RUN  apk install -y findutils && apt-get install -y xargs
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17

#build이미지에서 build/libs/*.jar 파일을 app.jar로 복사
COPY --from=builder build/libs/*.jar app.jar

# /tmp를 볼륨으로 지정
VOLUME /tmp
#app.jar를 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
