FROM maven:3.6-openjdk-17-slim AS BUILD
COPY ./src src/
COPY ./pom.xml pom.xml
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:17-jdk-alpine
#ARG JAR_FILE=./target/*.jar
#COPY $JAR_FILE app.jar
COPY --from=BUILD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
