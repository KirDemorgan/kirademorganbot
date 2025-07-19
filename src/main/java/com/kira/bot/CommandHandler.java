package com.kira.bot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Handles bot commands in Kira's style
 */
@Slf4j
public class CommandHandler {
    
    private static final List<String> HELP_RESPONSES = Arrays.asList(
        "Я Kira. Помогаю с кодом и техническими вопросами. Просто спроси что-нибудь.",
        "Нужна помощь? Задавай вопросы по программированию. Я разбираюсь в Java, Python, Rust.",
        "Доступные команды: !help, !about, !status. Или просто пиши - отвечу если смогу помочь."
    );
    
    public boolean handleCommand(MessageReceivedEvent event, String message) {
        // Check if message starts with command prefix (default "!")
        String prefix = System.getenv("BOT_PREFIX");
        if (prefix == null || prefix.isEmpty()) {
            prefix = "!"; // Default prefix
        }
        
        if (!message.startsWith(prefix)) {
            return false;
        }
        
        String command = message.toLowerCase().split(" ")[0];
        
        switch (command) {
            case "!help":
            case "!помощь":
                handleHelp(event);
                return true;
                
            case "!about":
            case "!инфо":
                handleAbout(event);
                return true;
                
            case "!status":
            case "!статус":
                handleStatus(event);
                return true;
                
            case "!ping":
                handlePing(event);
                return true;
                
            default:
                return false;
        }
    }
    
    private void handleHelp(MessageReceivedEvent event) {
        String response = HELP_RESPONSES.get((int) (Math.random() * HELP_RESPONSES.size()));
        event.getChannel().sendMessage(response).queue();
        log.info("Help command executed by {}", event.getAuthor().getName());
    }
    
    private void handleAbout(MessageReceivedEvent event) {
        String response = """
            **Kira Demorgan** - младший разработчик, студентка MIT
            
            🎓 Computer Science @ MIT
            💻 Языки: Java, Python, Rust, C
            🏢 Работаю в стартапе
            🌟 Opensource contributor
            
            Помогаю с кодом, архитектурой, отладкой. Без лишних слов, только по делу.
            """;
        event.getChannel().sendMessage(response).queue();
        log.info("About command executed by {}", event.getAuthor().getName());
    }
    
    private void handleStatus(MessageReceivedEvent event) {
        long uptime = System.currentTimeMillis() - getStartTime();
        String uptimeStr = formatUptime(uptime);
        
        String response = String.format("""
            **Статус системы:**
            ✅ Онлайн
            ⏱️ Аптайм: %s
            🧠 Gemini API: подключен
            📊 JVM: %d MB используется
            
            Всё работает. Можно кодить.
            """, uptimeStr, getUsedMemoryMB());
            
        event.getChannel().sendMessage(response).queue();
        log.info("Status command executed by {}", event.getAuthor().getName());
    }
    
    private void handlePing(MessageReceivedEvent event) {
        long start = System.currentTimeMillis();
        event.getChannel().sendMessage("Проверяю связь...").queue(message -> {
            long ping = System.currentTimeMillis() - start;
            message.editMessage(String.format("Pong! Задержка: %d ms", ping)).queue();
        });
        log.info("Ping command executed by {}", event.getAuthor().getName());
    }
    
    private static long startTime = System.currentTimeMillis();
    
    private long getStartTime() {
        return startTime;
    }
    
    private String formatUptime(long uptime) {
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%d дн. %d ч.", days, hours % 24);
        } else if (hours > 0) {
            return String.format("%d ч. %d мин.", hours, minutes % 60);
        } else {
            return String.format("%d мин.", minutes);
        }
    }
    
    private long getUsedMemoryMB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }
}