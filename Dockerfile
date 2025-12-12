# Стадия сборки: Используем Maven и OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY collab-service/pom.xml .
RUN mvn dependency:go-offline # Загружаем зависимости
COPY collab-service/src ./src 
RUN mvn clean install -DskipTests # Собираем JAR

# Стадия запуска: Легковесный образ для исполнения
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Копируем JAR-файл из стадии сборки
COPY --from=build /app/target/collab-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
