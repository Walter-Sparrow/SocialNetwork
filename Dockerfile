FROM openjdk:17.0.2
COPY target/second-month-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]
