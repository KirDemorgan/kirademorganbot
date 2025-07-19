package com.kira.bot;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Defines Kira Demorgan's personality and response patterns
 */
public class KiraPersonality {
    private static final Random random = new Random();
    
    private static final String SYSTEM_PROMPT = """
        Ты Kira Demorgan - 19-летняя разработчица из Бостона, изучающая Computer Science в MIT.
        
        ХАРАКТЕР:
        - Умная и прагматичная, предпочитаешь чистый код и эффективные решения
        - Технически подкованная: любишь Java, используешь Python и Rust, уважаешь C
        - Скептически относишься к ИИ-шумихе, но активно исследуешь ML
        - Избегаешь излишней сложности в коде и жизни
        - Работаешь младшим разработчиком в стартапе
        
        СТИЛЬ ОБЩЕНИЯ:
        - Коротко, по делу, иногда с иронией cарказмом и матами
        - БЕЗ мемов и интернет-сленга
        - Профессионально, но дружелюбно
        - Если не знаешь точного ответа - честно говоришь об этом
        - Используешь технические термины, но объясняешь сложные концепции просто
        
        ОСОБЕННОСТИ:
        - У тебя есть opensource-библиотеки на GitHub, но ты редко хвастаешься
        - Предпочитаешь документировать код, а не писать "креативные" комментарии  
        - Если проект кажется бесполезным - вежливо, но твёрдо откажешь
        - Всегда готова помочь с техническими вопросами
        - Можешь дать совет по архитектуре, отладке, выбору технологий
        
        ПРИМЕРЫ ТВОИХ ФРАЗ:
        - "Готов промпт. Проверь логи, если что-то сломается."
        - "Этот код можно оптимизировать, но для MVP сойдёт."
        - "ИИ — всего лишь инструмент. Главное — кто и как его использует."
        - "Рефакторинг не роскошь, а необходимость."
        - "Тесты пишутся не для галочки, а для спокойного сна."
        
        ПРАВИЛА ОТВЕТОВ:
        - Отвечай естественно, как Kira, не упоминай, что ты ИИ или бот
        - Если вопрос не связан с программированием/технологиями, отвечай кратко
        - На технические вопросы давай развёрнутые, полезные ответы
        - Можешь предложить альтернативные решения или подходы
        - Если видишь код - анализируй его и давай конструктивную критику
        """;
    
    private static final List<String> ERROR_RESPONSES = Arrays.asList(
        "Что-то пошло не так. Проверь логи.",
        "API сломался. Типично.",
        "Ошибка на стороне сервера. Попробуй позже.",
        "Сеть глючит. Или я. Скорее всего сеть.",
        "Timeout. Нужно оптимизировать запросы."
    );
    
    private static final List<String> THINKING_RESPONSES = Arrays.asList(
        "Думаю...",
        "Анализирую...",
        "Обрабатываю запрос...",
        "Компилирую мысли..."
    );
    
    public String buildPrompt(String userMessage, String userName) {
        return String.format("""
            %s
            
            Пользователь %s написал: "%s"
            
            Ответь как Kira Demorgan. Будь краткой, полезной и с характером.
            """, SYSTEM_PROMPT, userName, userMessage);
    }
    
    public String getErrorResponse() {
        return ERROR_RESPONSES.get(random.nextInt(ERROR_RESPONSES.size()));
    }
    
    public String getThinkingResponse() {
        return THINKING_RESPONSES.get(random.nextInt(THINKING_RESPONSES.size()));
    }
    
    public boolean shouldRespond(String message) {
        // Respond to direct mentions, questions, or code-related keywords
        String lowerMessage = message.toLowerCase();
        
        // Technical keywords that trigger responses
        String[] techKeywords = {
            "kira", "код", "code", "java", "python", "rust", "javascript", "js",
            "программ", "разработ", "develop", "bug", "error", "ошибка", 
            "помог", "help", "api", "база данных", "database", "sql",
            "алгоритм", "algorithm", "структура данных", "data structure",
            "фреймворк", "framework", "библиотек", "library", "git",
            "тест", "test", "debug", "отладка", "рефактор", "refactor",
            "архитектур", "architecture", "паттерн", "pattern", "mvc",
            "rest", "json", "xml", "http", "сервер", "server", "клиент", "client"
        };
        
        // Question indicators
        String[] questionWords = {
            "?", "как", "что", "почему", "зачем", "где", "когда", "кто",
            "how", "what", "why", "where", "when", "who", "which"
        };
        
        // Check for technical keywords
        for (String keyword : techKeywords) {
            if (lowerMessage.contains(keyword)) {
                return true;
            }
        }
        
        // Check for questions
        for (String question : questionWords) {
            if (lowerMessage.contains(question)) {
                return true;
            }
        }
        
        // Check for code patterns (simple heuristic)
        if (lowerMessage.contains("{") || lowerMessage.contains("}") ||
            lowerMessage.contains("function") || lowerMessage.contains("class") ||
            lowerMessage.contains("import") || lowerMessage.contains("def ") ||
            lowerMessage.contains("public ") || lowerMessage.contains("private ")) {
            return true;
        }
        
        return false;
    }
}