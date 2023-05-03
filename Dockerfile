FROM openjdk:17-oracle
EXPOSE 8080
ADD demo-0.0.2-SNAPSHOT.jar app.jar
ENTRYPOINT [“java”,“-jar”, “app.jar”]
