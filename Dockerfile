FROM amazoncorretto:17-alpine-jdk
COPY src/target/users-api-0.0.1-SNAPSHOT.jar nicode-users-app.jar
ENTRYPOINT ["java", "-jar", "nicode-users-app.jar"]