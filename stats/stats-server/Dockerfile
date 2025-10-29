# Этап 1: сборка приложения с Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Копируем ВЕСЬ проект (из корня проекта)
COPY ../../ .

# Собираем только stats-server модуль
RUN mvn clean package -DskipTests -pl stats-server -am

# Этап 2: запуск приложения
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем JAR из stats-server модуля
COPY --from=builder /app/stats-server/target/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]