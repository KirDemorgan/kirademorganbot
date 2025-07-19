@echo off
echo Starting Kira Discord Bot...

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if .env file exists
if not exist .env (
    echo Error: .env file not found
    echo Please copy .env.example to .env and configure it
    pause
    exit /b 1
)

REM Create logs directory if it doesn't exist
if not exist logs mkdir logs

REM Build the project if jar doesn't exist
if not exist target\discord-bot-1.0.0.jar (
    echo Building project...
    mvn clean package -q
    if %errorlevel% neq 0 (
        echo Error: Build failed
        pause
        exit /b 1
    )
)

REM Start the bot
echo Bot starting...
java -jar target\discord-bot-1.0.0.jar

pause