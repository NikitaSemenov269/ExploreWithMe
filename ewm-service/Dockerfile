# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy
# Копируем JAR-файл
COPY target/*.jar ewm-service.jar
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/ewm-service.jar"]