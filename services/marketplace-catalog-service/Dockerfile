FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY .m2 /root/.m2

COPY adapters/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
