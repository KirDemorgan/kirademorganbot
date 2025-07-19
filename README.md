# Kira Demorgan Discord Bot

Простой Discord бот с характером Kira Demorgan - прагматичной разработчицы из MIT. Использует Google Gemini API для генерации ответов.

## Особенности

- 🤖 Персонализированные ответы в стиле Kira Demorgan
- 🧠 Интеграция с Google Gemini API
- 📝 Умное разделение длинных сообщений
- 🔒 Безопасное хранение секретов в .env файле
- 📊 Логирование и мониторинг
- ⚡ Асинхронная обработка сообщений

## Быстрый старт

### 1. Предварительные требования

- Java 17 или выше
- Maven 3.6+
- Discord бот токен
- Google Gemini API ключ

### 2. Автоматический запуск

```bash
# Windows
start.bat

# Linux/Mac
./start.sh
```

Скрипты автоматически проверят зависимости, соберут проект и запустят бота.

### 3. Создание Discord бота

1. Перейди на [Discord Developer Portal](https://discord.com/developers/applications)
2. Создай новое приложение
3. В разделе "Bot" создай бота и скопируй токен
4. В разделе "OAuth2 > URL Generator":
   - Выбери scope: `bot`
   - Выбери permissions: `Send Messages`, `Read Message History`
5. Пригласи бота на сервер по сгенерированной ссылке

### 4. Получение Gemini API ключа

1. Перейди на [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Создай новый API ключ
3. Скопируй ключ для использования

### 5. Настройка проекта

```bash
# Клонируй или скачай проект
git clone <your-repo-url>
cd discord-bot

# Создай .env файл из примера
cp .env.example .env
```

Отредактируй `.env` файл:
```env
DISCORD_TOKEN=твой_discord_bot_token
CHANNEL_ID=id_канала_где_работает_бот
GEMINI_API_KEY=твой_gemini_api_key
```

### 6. Запуск

```bash
# Сборка проекта
mvn clean package

# Запуск бота
java -jar target/discord-bot-1.0.0.jar
```

Или для разработки:
```bash
mvn exec:java -Dexec.mainClass="com.kira.bot.KiraBot"
```

## Как получить Channel ID

1. Включи режим разработчика в Discord (User Settings > Advanced > Developer Mode)
2. Правый клик на канал → "Copy ID"
3. Вставь ID в .env файл

## Конфигурация

Все настройки хранятся в `.env` файле:

- `DISCORD_TOKEN` - токен Discord бота
- `CHANNEL_ID` - ID канала для работы бота
- `GEMINI_API_KEY` - ключ Google Gemini API
- `BOT_PREFIX` - префикс команд (опционально)
- `MAX_MESSAGE_LENGTH` - максимальная длина сообщения

## Команды бота

- `!help` или `!помощь` - показать справку
- `!about` или `!инфо` - информация о Kira
- `!status` или `!статус` - статус системы
- `!ping` - проверка связи

## Характер Kira

Бот отвечает в стиле Kira Demorgan:
- Коротко и по делу
- С лёгкой иронией, но без мемов
- Технически грамотно
- Прагматично и эффективно

Примеры фраз:
- "Готов промпт. Проверь логи, если что-то сломается."
- "Этот код можно оптимизировать, но для MVP сойдёт."
- "ИИ — всего лишь инструмент. Главное — кто и как его использует."

Бот автоматически реагирует на:
- Упоминания (@Kira)
- Вопросы (содержащие ?, как, что, почему)
- Технические термины (код, Java, Python, bug, error)
- Фрагменты кода

## Структура проекта

```
src/
├── main/java/com/kira/bot/
│   ├── KiraBot.java           # Главный класс
│   ├── MessageListener.java   # Обработчик сообщений
│   ├── GeminiService.java     # Сервис для Gemini API
│   └── KiraPersonality.java   # Логика персонажа
└── main/resources/
    └── logback.xml            # Конфигурация логирования
```

## Логи

Логи сохраняются в папку `logs/`:
- `kira-bot.log` - текущий лог
- `kira-bot.YYYY-MM-DD.log` - архивные логи

## Устранение проблем

### Бот не отвечает
- Проверь правильность токена и Channel ID
- Убедись, что у бота есть права на чтение и отправку сообщений
- Проверь логи на ошибки

### Ошибки Gemini API
- Проверь правильность API ключа
- Убедись, что не превышен лимит запросов
- Проверь интернет-соединение

### Проблемы с зависимостями
```bash
mvn clean install -U
```

## Разработка

Для добавления новых функций:

1. Модифицируй `KiraPersonality.java` для изменения характера
2. Обнови `MessageListener.java` для новой логики обработки
3. Расширь `GeminiService.java` для дополнительных API вызовов

## Лицензия

MIT License - используй как хочешь, но на свой страх и риск.

---

*"Код работает? Отлично. Не работает? Проверь логи." - Kira Demorgan*