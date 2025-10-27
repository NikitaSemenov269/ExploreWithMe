FROM eclipse-temurin:21-jre-jammy
COPY ./target/ewm-service-*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["stats-server:9090", "--", "java", "-jar", "app.jar"]