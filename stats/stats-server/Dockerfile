# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy
# Копируем JAR-файл
COPY target/*.jar stats-server.jar
# Открываем порт
EXPOSE 9090
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/stats-server.jar"]