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

