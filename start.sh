#!/bin/bash

echo "Starting Kira Discord Bot..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo "Error: .env file not found"
    echo "Please copy .env.example to .env and configure it"
    exit 1
fi

# Create logs directory if it doesn't exist
mkdir -p logs

# Build the project if jar doesn't exist
if [ ! -f target/discord-bot-1.0.0.jar ]; then
    echo "Building project..."
    mvn clean package -q
    if [ $? -ne 0 ]; then
        echo "Error: Build failed"
        exit 1
    fi
fi

# Start the bot
echo "Bot starting..."
java -jar target/discord-bot-1.0.0.jar