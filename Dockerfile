FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
# Build application
RUN mvn clean install

FROM openjdk:17-alpine
COPY --from=build /app/target/qieam.jar qieam.jar
ENTRYPOINT ["java","-jar","/qieam.jar"]
