# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy
# Задаём рабочий каталог
WORKDIR /app
# Копируем JAR-файл
COPY pom.xml .
COPY src ./src
RUN mvn clean package
# Запускаем приложение (исправленная версия)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar ewm-service.jar
ENTRYPOINT ["java", "-jar", "/ewm-service.jar"]