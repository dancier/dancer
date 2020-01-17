FROM openjdk:12-jdk-alpine
RUN addgroup -S spring && adduser -S dancier -G dancier
USER dancier:dancier
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]