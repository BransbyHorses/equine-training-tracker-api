# equine-training-tracker-api

Spring Boot REST API for Bransby equine training tracker application.

### Prerequisites

  * Java 17
  * Docker

### Set up .env

Install and configure [Intellij EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) to use environment variables. Add the following variables to an .env file:

```bash
# Postgres environment variables
DB_USER=
DB_PASSWORD=
DB_NAME=
# project environment variables
DATASOURCE=
```
### Build & Run Application (Docker)

```bash
$ docker-compose up --build -d
```

Setup and run the [front-end](https://github.com/BransbyHorses/equine-training-tracker-app).

### Swagger Services

Find Swagger API documentation at /swagger-ui/#/