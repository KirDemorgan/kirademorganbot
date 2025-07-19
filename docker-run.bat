@echo off
echo 🤖 Kira Discord Bot - Docker Setup
echo ==================================

REM Проверка наличия .env файла
if not exist .env (
    echo ❌ Файл .env не найден!
    echo 📝 Создай .env файл из .env.example:
    echo    copy .env.example .env
    echo    # Затем отредактируй .env с твоими токенами
    pause
    exit /b 1
)

REM Проверка наличия Docker
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker не установлен!
    echo 📦 Установи Docker: https://docs.docker.com/get-docker/
    pause
    exit /b 1
)

REM Проверка наличия Docker Compose
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose не установлен!
    echo 📦 Установи Docker Compose: https://docs.docker.com/compose/install/
    pause
    exit /b 1
)

REM Создание директории для логов
if not exist logs mkdir logs

echo 🔨 Сборка Docker образа...
docker-compose build

if %errorlevel% equ 0 (
    echo ✅ Образ собран успешно!
    echo 🚀 Запуск Kira бота...
    docker-compose up -d
    
    echo.
    echo ✅ Kira бот запущен!
    echo 📊 Проверить статус: docker-compose ps
    echo 📋 Посмотреть логи: docker-compose logs -f kira-bot
    echo 🛑 Остановить бота: docker-compose down
    echo.
    echo 🔍 Мониторинг логов (опционально):
    echo    docker-compose --profile monitoring up -d
    echo    Затем открой http://localhost:8080
) else (
    echo ❌ Ошибка при сборке образа!
    pause
    exit /b 1
)

pause