FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY ./ ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
RUN ./gradlew bootJar --no-daemon

EXPOSE 6767

CMD ["java", "-jar", "build/libs/sgs-server.jar"]