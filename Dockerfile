FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
RUN mvn --batch-mode dependency:go-offline

COPY src ./src
RUN mvn --batch-mode clean package -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S volta && adduser -S volta -G volta

COPY --from=build /build/target/*.jar app.jar

RUN chown -R volta:volta /app
USER volta

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]