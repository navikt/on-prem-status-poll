FROM cgr.dev/chainguard/jdk:latest AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY src/ src/
RUN ./mvnw clean package -DskipTests

FROM cgr.dev/chainguard/jre:latest

WORKDIR /app

COPY --from=build /app/target/on-prem-status-poll-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
