package com.kira.bot;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * Kira Demorgan Discord Bot
 * A pragmatic AI-powered chat bot with personality
 */
@Slf4j
public class KiraBot {
    
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            String token = dotenv.get("DISCORD_TOKEN");
            String channelId = dotenv.get("CHANNEL_ID");
            String geminiApiKey = dotenv.get("GEMINI_API_KEY");
            
            if (token == null || channelId == null || geminiApiKey == null) {
                log.error("Missing required environment variables. Check your .env file.");
                System.exit(1);
            }
            
            GeminiService geminiService = new GeminiService(geminiApiKey);
            
            JDA jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.customStatus("Coding in Java ☕"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new MessageListener(channelId, geminiService))
                    .build();
            
            jda.awaitReady();
            log.info("Kira bot is online and ready!");
            
        } catch (Exception e) {
            log.error("Failed to start bot", e);
            System.exit(1);
        }
    }
}