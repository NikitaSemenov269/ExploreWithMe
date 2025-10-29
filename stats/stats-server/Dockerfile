# Базовый образ с Java 21
FROM eclipse-temurin:21-jre-jammy as builder
# Задаём рабочий каталог
WORKDIR /app
# Копируем JAR-файл
COPY pom.xml .
COPY src ./src
RUN mvn clean package
# Запускаем приложение
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar stats-server.jar
ENTRYPOINT ["java", "-jar", "/stats-server.jar"]