
FROM openjdk:17-slim

ADD ./target/dancer-2.0-SNAPSHOT.jar /dancer-2.0-SNAPSHOT.jar
ADD ./config.yml /config.yml
ADD ./src/main/resources/data/images /data/images
CMD ["java",  "-jar", "/dancer-2.0-SNAPSHOT.jar", "server", "config.yml"]
EXPOSE 8080