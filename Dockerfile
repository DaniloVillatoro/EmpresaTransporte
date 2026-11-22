# Etapa 1: Compilar el .war con Maven y Java 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Desplegar en Apache Tomcat 11 con JDK 17
FROM tomcat:11.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/mavenproject3-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/mavenproject3.war

EXPOSE 8080
CMD ["catalina.sh", "run"]