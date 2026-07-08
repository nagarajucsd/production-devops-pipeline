FROM eclipse-temurin:17-jre

LABEL maintainer="Dandu Rama Siva Naga Raju"

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring

COPY app/springboot-app/target/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]