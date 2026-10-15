FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S -g 10001 app && adduser -S -D -H -u 10001 -G app app
WORKDIR /app
COPY --from=build --chown=10001:10001 /build/target/tarea2-sanhueza-torres-0.0.1-SNAPSHOT.jar /app/app.jar
ENV APP_PORT=3001 \
    APP_ENV=production \
    CSV_PATH=/data/compras.csv
EXPOSE 3001
USER 10001:10001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
