FROM eclipse-temurin:17-jre

WORKDIR /app

COPY app/springboot-app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
