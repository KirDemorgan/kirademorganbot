# Multi-stage build для оптимизации размера образа
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Установка рабочей директории
WORKDIR /app

# Копирование файлов проекта
COPY pom.xml .
COPY src ./src

# Сборка проекта
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-alpine

# Установка необходимых пакетов
RUN apk add --no-cache tzdata

# Создание пользователя для безопасности
RUN addgroup -g 1001 -S kira && \
    adduser -S kira -u 1001 -G kira

# Установка рабочей директории
WORKDIR /app

# Создание директории для логов
RUN mkdir -p logs && chown -R kira:kira logs

# Копирование собранного jar файла
COPY --from=builder /app/target/discord-bot-*.jar app.jar

# Изменение владельца файлов
RUN chown -R kira:kira /app

# Переключение на пользователя kira
USER kira

# Настройка переменных окружения
ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"
ENV TZ=Europe/Moscow

# Открытие порта для мониторинга (опционально)
EXPOSE 8080

# Проверка здоровья контейнера
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD pgrep -f "java.*app.jar" > /dev/null || exit 1

# Запуск приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]