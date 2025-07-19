#!/bin/bash

echo "🤖 Kira Discord Bot - Docker Setup"
echo "=================================="

# Проверка наличия .env файла
if [ ! -f .env ]; then
    echo "❌ Файл .env не найден!"
    echo "📝 Создай .env файл из .env.example:"
    echo "   cp .env.example .env"
    echo "   # Затем отредактируй .env с твоими токенами"
    exit 1
fi

# Проверка наличия Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не установлен!"
    echo "📦 Установи Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

# Проверка наличия Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose не установлен!"
    echo "📦 Установи Docker Compose: https://docs.docker.com/compose/install/"
    exit 1
fi

# Создание директории для логов
mkdir -p logs

echo "🔨 Сборка Docker образа..."
docker-compose build

if [ $? -eq 0 ]; then
    echo "✅ Образ собран успешно!"
    echo "🚀 Запуск Kira бота..."
    docker-compose up -d
    
    echo ""
    echo "✅ Kira бот запущен!"
    echo "📊 Проверить статус: docker-compose ps"
    echo "📋 Посмотреть логи: docker-compose logs -f kira-bot"
    echo "🛑 Остановить бота: docker-compose down"
    echo ""
    echo "🔍 Мониторинг логов (опционально):"
    echo "   docker-compose --profile monitoring up -d"
    echo "   Затем открой http://localhost:8080"
else
    echo "❌ Ошибка при сборке образа!"
    exit 1
fi