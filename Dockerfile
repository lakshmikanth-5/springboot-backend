FROM eclipse-temurin:8-ubi10-minimal
COPY target/*.jar /app.jar
CMD ["java", "-jar", "app.jar"]
