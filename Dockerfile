FROM maven:3.8.6-amazoncorretto-17 AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src
RUN mvn package

FROM openjdk:17
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/docker-test/order.jar
ENTRYPOINT ["java","-jar","/opt/docker-test/order.jar"]