FROM gradle:8.5-jdk17 AS builder
COPY . /usr/src
WORKDIR /usr/src
RUN gradle wrapper --gradle-version 8.5
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-alpine
COPY --from=builder /usr/src/build/libs/pingleserver-0.0.1-SNAPSHOT.jar /usr/app/app.jar
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar", "--spring.profiles.active=dev"]