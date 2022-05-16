# equine-training-tracker-api

Spring Boot REST API for Bransby equine training tracker application. Intialized with Spring Initializr.

## Run the application

### Prerequisites

  * Java 11+
  * Docker

### Set up Postgres services
Run postgres database and PGAdmin in Docker:
```bash
$ docker-compose up -d
```
Start postgres database only:
```bash
$ docker-compose up -d postgresql_database
```
Remove postgres data persisted in the volume:
```bash
$ docker-compose down -v
$ docker volume rm equine-training-tracker-api_database-data
```