FROM eclipse-temurin:21-jre-jammy
COPY ./target/ewm-service-*.jar ./app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
EXPOSE 8080
ENTRYPOINT ["/wait-for-it.sh", "stats-server:9090", "--", "java", "-jar", "app.jar"]