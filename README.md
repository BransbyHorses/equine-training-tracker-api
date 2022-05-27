# equine-training-tracker-api

Spring Boot REST API for Bransby equine training tracker application. Intialized with Spring Initializr.

## Run the application

### Prerequisites

  * Java 11+
  * Docker

### Set up Postgres services

Install and configure [Intellij EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) to use environment variables. Add the following variables to an .env file:

```bash
# Postgres environment variables
DB_USER=
DB_PASSWORD=
DB_NAME=
```

Run a postgres database and PGAdmin in Docker:
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
 
or 

$ docker-compose down
$ docker volume rm equine-training-tracker-api_database-data
```

### Docker Issues (Mac Only)

To see all live docker containers types that are currently active, run the follwing line in your terminal:
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


