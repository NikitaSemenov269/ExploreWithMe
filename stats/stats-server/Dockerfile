# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy
# Копируем JAR-файл
COPY target/*.jar stats-server.jar
# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java", "-jar", "/stats-server.jar"]