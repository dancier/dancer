
FROM openjdk:21-slim

ADD target/dancer.jar /dancer.jar
CMD ["java", "--enable-preview", "-jar", "/dancer.jar"]
EXPOSE 8080