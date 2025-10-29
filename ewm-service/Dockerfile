FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Копируем ВЕСЬ проект (из корня проекта)
COPY ../../ .

# Собираем только ewm-service модуль
RUN mvn clean package -DskipTests -pl ewm-service -am

# Этап 2: запуск приложения
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем JAR из ewm-service модуля
COPY --from=builder /app/ewm-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]