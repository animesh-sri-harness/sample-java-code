# Intentional older base image for the Trivy scan stage in the demo.
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/harness-ci-cd-demo-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
