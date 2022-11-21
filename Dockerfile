
FROM openjdk:17-slim

ADD target/dancer.jar /dancer.jar
CMD ["java", "--enable-preview", "-jar", "/dancer.jar"]
EXPOSE 8080