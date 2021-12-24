# syntax=docker/dockerfile:1
FROM openjdk:16-alpine
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN ./gradlew build
ENTRYPOINT ["java","-jar","build/libs/GifExchangeRate-0.0.1-SNAPSHOT.jar"]
