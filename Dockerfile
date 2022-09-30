FROM --platform=linux/amd64 maven:3.6-openjdk-17-slim AS BUILD
COPY ./src src/
COPY ./pom.xml pom.xml
RUN mvn clean package -Dmaven.test.skip

FROM --platform=linux/amd64 openjdk:17-jdk-alpine
COPY --from=BUILD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
