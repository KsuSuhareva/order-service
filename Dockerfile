FROM openjdk:17-jdk-alpine
COPY target/*.jar order-service.jar
EXPOSE 7080
ENTRYPOINT ["java","-jar","order-service.jar"]