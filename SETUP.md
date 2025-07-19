# Пошаговая инструкция по настройке Kira Discord Bot

## Шаг 1: Подготовка окружения

### Установка Java
```bash
# Проверь версию Java (нужна 17+)
java -version

# Если Java нет, скачай с https://adoptium.net/
```

### Установка Maven
```bash
# Проверь Maven
mvn -version

# Если нет, скачай с https://maven.apache.org/download.cgi
```

## Шаг 2: Создание Discord бота

### 2.1 Создание приложения
1. Открой [Discord Developer Portal](https://discord.com/developers/applications)
2. Нажми "New Application"
3. Введи имя: "Kira Demorgan Bot"
4. Нажми "Create"

### 2.2 Настройка бота
1. Перейди в раздел "Bot" (слева)
2. Нажми "Add Bot" → "Yes, do it!"
3. В разделе "Token" нажми "Copy" и сохрани токен
4. Включи "Message Content Intent" (важно!)

### 2.3 Приглашение бота
1. Перейди в "OAuth2" → "URL Generator"
2. Выбери scopes: `bot`
3. Выбери Bot Permissions:
   - Send Messages
   - Read Message History
   - Use Slash Commands (опционально)
4. Скопируй сгенерированную ссылку
5. Открой ссылку и пригласи бота на свой сервер

## Шаг 3: Получение Gemini API ключа

### 3.1 Создание ключа
1. Перейди на [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Войди в Google аккаунт
3. Нажми "Create API Key"
4. Выбери проект или создай новый
5. Скопируй созданный ключ

### 3.2 Проверка ключа
```bash
# Тестовый запрос (замени YOUR_API_KEY)
curl -X POST \
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=YOUR_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{"contents":[{"parts":[{"text":"Hello"}]}]}'
```

## Шаг 4: Настройка проекта

### 4.1 Получение Channel ID
1. В Discord включи Developer Mode:
   - User Settings → Advanced → Developer Mode
2. Правый клик на нужный канал → "Copy ID"
3. Сохрани этот ID

### 4.2 Создание .env файла
```bash
# Скопируй пример
cp .env.example .env

# Отредактируй .env файл
```

Содержимое `.env`:
```env
DISCORD_TOKEN=твой_discord_bot_token_здесь
CHANNEL_ID=id_канала_здесь
GEMINI_API_KEY=твой_gemini_api_key_здесь
BOT_PREFIX=!
MAX_MESSAGE_LENGTH=2000
```

## Шаг 5: Сборка и запуск

### 5.1 Сборка проекта
```bash
# Установка зависимостей и сборка
mvn clean package

# Если есть ошибки, попробуй:
mvn clean install -U
```

### 5.2 Запуск бота
```bash
# Запуск собранного jar файла
java -jar target/discord-bot-1.0.0.jar

# Или для разработки:
mvn exec:java -Dexec.mainClass="com.kira.bot.KiraBot"
```

### 5.3 Проверка работы
1. Бот должен появиться онлайн в Discord
2. Напиши что-нибудь в настроенном канале
3. Бот должен ответить в стиле Kira

## Шаг 6: Устранение проблем

### Проблема: "Invalid Token"
- Проверь правильность токена в .env
- Убедись, что токен скопирован полностью
- Пересоздай токен в Discord Developer Portal

### Проблема: "Missing Access"
- Проверь права бота на сервере
- Убедись, что включен "Message Content Intent"
- Проверь права канала для бота

### Проблема: "Gemini API Error"
- Проверь правильность API ключа
- Убедись, что API включен в Google Cloud Console
- Проверь лимиты запросов

### Проблема: Бот не отвечает
- Проверь правильность Channel ID
- Убедись, что бот видит сообщения в канале
- Проверь логи в папке `logs/`

## Шаг 7: Дополнительные настройки

### Автозапуск (Linux/Mac)
Создай systemd сервис:
```bash
sudo nano /etc/systemd/system/kira-bot.service
```

```ini
[Unit]
Description=Kira Discord Bot
After=network.target

[Service]
Type=simple
User=твой_пользователь
WorkingDirectory=/путь/к/проекту
ExecStart=/usr/bin/java -jar target/discord-bot-1.0.0.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl enable kira-bot
sudo systemctl start kira-bot
```

### Мониторинг логов
```bash
# Просмотр логов в реальном времени
tail -f logs/kira-bot.log

# Поиск ошибок
grep ERROR logs/kira-bot.log
```

## Готово!

Теперь у тебя есть работающий Discord бот с характером Kira Demorgan. Бот будет отвечать на сообщения в указанном канале, используя Google Gemini для генерации ответов в стиле прагматичной разработчицы.

Если что-то не работает - проверь логи и убедись, что все токены и ID правильные.