FROM maven:3.6.0-jdk-13 AS BUILD
COPY src ./src
COPY pom.xml .
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=./target/*.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
