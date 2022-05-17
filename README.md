# equine-training-tracker-api

Spring Boot REST API for Bransby equine training tracker application. Intialized with Spring Initializr.

## Run the application

### Prerequisites

  * Java 11+
  * Docker

### Set up Postgres services

Install [Intellij EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) to use .env variables

Run postgres database and PGAdmin in Docker:
```bash
$ docker-compose up -d
```
or run the postgres database only:
```bash
$ docker-compose up -d postgresql_database
```
Remove data persisted in the postgres volume with either:
```bash
$ docker-compose down -v
$ docker volume rm equine-training-tracker-api_database-data
```