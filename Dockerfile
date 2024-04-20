FROM amazoncorretto:17-alpine
VOLUME /tmp
COPY /production-tracker/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
