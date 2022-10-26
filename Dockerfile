
FROM openjdk:17-slim

ADD ./target/dancer.jar /dancer.jar
CMD ["java",  "-jar", "/dancer.jar", "--enable-preview"]
EXPOSE 8080