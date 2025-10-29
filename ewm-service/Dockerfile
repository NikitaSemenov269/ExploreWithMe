# Этап 1: сборка приложения с Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и исходники
COPY ../../ .

# Скачиваем зависимости и собираем JAR
RUN mvn clean package -pl ewm-service -am

# Этап 2: запуск приложения в минимальном окружении
FROM eclipse-temurin:21-jre-jammy

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из этапа сборки
COPY --from=builder /app/target/*.jar ewm-service.jar

# Открываем порт (укажите нужный, например 8080)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "ewm-service.jar"]