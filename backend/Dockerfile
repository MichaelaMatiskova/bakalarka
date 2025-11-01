FROM eclipse-temurin:17-jdk
COPY target/backend.jar app.jar
RUN mkdir -p /app/output && chmod 777 /app/output
ENTRYPOINT ["java", "-jar", "/app.jar"]
