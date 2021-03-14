FROM openjdk:13-slim

ADD ./target/dancer-2.0-SNAPSHOT.jar /data/dancer-2.0-SNAPSHOT.jar
ADD ./config.yml /config.yml

CMD ["java",  "-jar", "/data/dancer-2.0-SNAPSHOT.jar", "server", "config.yml"]
EXPOSE 8080