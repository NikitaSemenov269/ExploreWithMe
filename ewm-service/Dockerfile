# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy
# Копируем JAR-файл
COPY ./target/ewm-service-*.jar ./app.jar
# Открываем порт
EXPOSE 8080
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]