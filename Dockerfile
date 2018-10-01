FROM openjdk:8-jdk-alpine
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "welsh-black-restapi.jar"]