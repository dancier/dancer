# Dancer

## How to start the Dancer application

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/dancer-2.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

## Health Check

To see your applications health enter url `http://localhost:8081/healthcheck` 

## Deploy

### Build the docker image
```bash
./docker-build-image.sh
```

### Push the docker image
```bash
./docker-push-image.sh
```

### Update the image on the target

scp docker-compose.yml .env root@dancer.dancier.net:/root/

docker-compose pull

docker-compose up -d --force-recreate

#### Database

docker exec -it root_postgres_1 psql -h localhost -U postgres

docker exec -it root_dancer_1 /bin/bash

## Developer notes

These are some useful hints for running the Dancer backend locally during development.

### Create .env

Copy file ".env_template" to ".env" and adapt the new file.

You may read this dotenv data via:
```bash
export $(grep -v '^#' .env | xargs)
```
But this will succeed only if the data is not too complex (e.g. containing spaces).

### Start docker image running PostgreSQL

```bash
docker pull postgres
docker run --name postgres-dancier -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```
If you prefer another port (e.g. 5433) use ```-p 5433:5432``` instead
and add the according ```DW_DB_JDBC_URL``` to your ".env".

### Setup database

#### Create database

```bash
docker exec -it postgres-dancier /bin/bash

psql -U postgres

create database dancier;
```

check via ```\l```

(exit psql and docker bash)

### Initalize database via Liquibase

(Requires successful Maven build and ".env" to be processed)

```bash
java -jar target/dancer-2.0-SNAPSHOT.jar db migrate config.yml
```
### Create Sessions for arbitrary users
To let the developer easily impersonate any user on non production systems, he can do the following.

Set config value login.devMode to _true_.

Invoke the login callback for facebook with an GET parameter called mocked_fb_user and the value of the foreign facebook id, you want to impersonate.

```bash
curl --request GET \
  --url 'http://localhost:8080/login/callback?mocked_fb_user=the-foreing-fb-id'
```

If the provided user with the id, does not exist, an user will be created. Otherwise the already existing one will be used.
