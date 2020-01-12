FROM openjdk:11-slim
ADD target/genuniv-enrolment-service.jar /jar/genuniv-enrolment-service.jar
EXPOSE 19103
ENTRYPOINT ["java", "-jar", "/jar/genuniv-enrolment-service.jar"]