#FROM amazoncorretto:17-alpine
#VOLUME /tmp
#COPY /production-tracker/target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#EXPOSE 8080

FROM maven:3.8.3-amazoncorretto-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
COPY --from=build /production-tracker/target/*.jar production-tracker.jar
ENTRYPOINT ["java","-jar","production-tracker.jar"]
EXPOSE 8080
