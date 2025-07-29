#
#FROM maven:3.9.11-amazoncorretto-21-debian-bookworm AS build
#
FROM maven:3.9-openjdk-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -DskipTests

#FROM eclipse-temurin:21-jre-alpine

# Alteracoes para criar imagem Raspiberry arm v7
FROM eclipse-temurin:21-jre

#Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/MoneyFlow-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java","-jar","app.jar"]
