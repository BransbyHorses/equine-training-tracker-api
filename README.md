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

## Prometheus and Grafana

* Prometheus
* Grafana
* Spring-Boot-Actuator
* Micrometer-Registry-Prometheus,
* postgres-exporter

[Prometheus](http://localhost:9090) "scrapes" information about the app's health and performance from HTTP endpoints.

It connects to [Grafana]((http://localhost:9000)), which visualises that data in graphs, tables and guages. Grafana collates these on dashboards that can be pre-loaded or customised.

Spring-Boot-Actuator exposes a number of endpoints that provide metrics. A full list of endpoints is available in the [documentation](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/production-ready-endpoints.html). 

The Micrometer and Postgres-exporter present those metrics in a format that Prometheus can scrape.

Grafana is configured with three example [dashboards](localhost:9000/dashboards). These are available on the [dashboards](http://localhost:9000/dashboards?query=) page.

    * JVM (Micrrometer) 
    * Postgres Overview (change the Instance field on the top row to point to the postgres-exporter port, currently 9187)
    * Spring-boot Application

Prometheus, Grafana and Postgres-exporter run as Docker containers.

Spring-Boot-Starter-Actuator and Micrometer-Registry-Prometheus are added as Maven dependencies. 

### application.properties config

A number of properties are required to configure prometheus and expose the endpoints for the actuator.

These are defined in the application.properties file.

### Actuator and Springfox incompatability workaround

Release 3.0.0 of Springfox, which is used for Swagger, is not directly compatible with the actuator.

A bean has been added to the app's entry point to resolve this issue, but the problem requires further investigation.


