FROM maven:3.6-openjdk-17-slim AS BUILD
WORKDIR build
COPY ./pom.xml ./
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-alpine
WORKDIR application
EXPOSE 8080
COPY --from=BUILD build/target/*.jar application/*.jar
ENTRYPOINT ["java","-jar","/app/*.jar"]
