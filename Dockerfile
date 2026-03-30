FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY life-os-domain/pom.xml life-os-domain/pom.xml
COPY life-os-memory/pom.xml life-os-memory/pom.xml
COPY life-os-rag/pom.xml life-os-rag/pom.xml
COPY life-os-tools/pom.xml life-os-tools/pom.xml
COPY life-os-agents/pom.xml life-os-agents/pom.xml
COPY life-os-orchestrator/pom.xml life-os-orchestrator/pom.xml
COPY life-os-infra/pom.xml life-os-infra/pom.xml
COPY life-os-web/pom.xml life-os-web/pom.xml

RUN mvn -pl life-os-web -am dependency:go-offline

COPY . .

RUN mvn -pl life-os-web -am package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=cluster

COPY --from=build /workspace/life-os-web/target/life-os-web-0.1.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
