FROM openjdk:17-alpine

EXPOSE 8080

ADD target/mailservice-0.0.1-SNAPSHOT.jar mailservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java" ,"-jar","mailservice-0.0.1-SNAPSHOT.jar"]
