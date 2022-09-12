# equine-training-tracker-api

Spring Boot REST API for Bransby equine training tracker application. Intialized with Spring Initializr.

## Run the application

### Prerequisites

  * Java 17
  * Docker

### Set up Postgres services

Install and configure [Intellij EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) to use environment variables. Add the following variables to an .env file:

```bash
# Postgres environment variables
DB_USER=
DB_PASSWORD=
DB_NAME=
# project environment variables
DATASOURCE=
```

## Docker

docker-compose.yml tells Docker which containers to run.

docker-compose up will run all of the containers specified in the file.

```bash
$ docker-compose up -d
```
To run a single container, pass its to docker-compose :
```bash
$ docker-compose up -d postgresql_database
```

### Removing postgres data from Docker

Remove data persisted in the postgres volume with either:
```bash
$ docker-compose down -v
```
or 

```
$ docker-compose down
$ docker volume rm equine-training-tracker-api_database-data
```

### Docker Issues (Mac Only)

To see all live docker containers types that are currently active, run the following line in your terminal:
```bash
$ docker ps
```
To see if port 5432 are already running on your machine run:
```bash
$ lsof -i :5432  
```
If you see the following in your terminal this means that the port is still active. Make a note of the PID number:
```bash
COMMAND  PID     USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
postgres 114 postgres    7u  IPv6 0x255cxxxxxx66571      0t0  TCP *:postgresql (LISTEN)
postgres 114 postgres    8u  IPv4 0x255cxxxxxx2bc0c1      0t0  TCP *:postgresql (LISTEN)
```
Then run the following line in your terminal replacing the 114 with the corresponding PID number of your instance: 
```bash
$ kill -9 114 

or 

$ sudo kill -9 114 

```
Then re-run:
```bash
$ docker-compose up -d
```

## Swagger Services

Run the project as above and then check {API_URL}/swagger-ui/#/ or run [locally](http://localhost:8080/swagger-ui/#/).

## Prometheus

Prometheus "scrapes" information about the app from HTTP endpoints.

It gathers, organises and stores these metrics to make it easier to understand how well the app is performing. 

Grafana visualises the info that Prometheus has scraped, generating graphs that represent the data.

### Dependencies
    
    * Prometheus 
    * Grafana
    * Spring-Boot-Actuator
    * Micrometer-Registry-Prometheus,

Prometheus runs as a Docker container and is defined as a service in the docker-compose file.

Spring-Boot-Starter-Actuator is added as a Maven dependency. 

The actuator gathers metrics from the app and exposes that information via http. 

Micrometer-Registry-Prometheus, also added via Maven, converts the actuator data into a format that Prometheus can scrape.

### application.properties config

A number of properties are required to configure prometheus and expose the endpoints for the actuator.

These are defined in the application.properties file.

### /actuator endpoints

Once configured, the app will expose a number of endpoints that provide performance/status data.

These include:

- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/metrics

The data that prometheus will scrape can be found on:

- http://localhost:8080/actuator/prometheus

- A full list of actuator endpoints is available in the [spring-boot documentation](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/production-ready-endpoints.html)

### Actuator and Springfox incompatability workaround

Release 3.0.0 of Springfox, which is used for Swagger implementation, is not directly compatible with the actuator.

A bean has been added to the app's main entry point to resolve this issue, but the problem requires further investigation.


