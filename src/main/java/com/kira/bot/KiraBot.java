package com.kira.bot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kira Demorgan Discord Bot
 * A pragmatic AI-powered chat bot with personality
 */
public class KiraBot {
    private static final Logger logger = LoggerFactory.getLogger(KiraBot.class);
    
    public static void main(String[] args) {
        try {
            // Load environment variables
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            String token = dotenv.get("DISCORD_TOKEN");
            String channelId = dotenv.get("CHANNEL_ID");
            String geminiApiKey = dotenv.get("GEMINI_API_KEY");
            
            if (token == null || channelId == null || geminiApiKey == null) {
                logger.error("Missing required environment variables. Check your .env file.");
                System.exit(1);
            }
            
            // Initialize Gemini service
            GeminiService geminiService = new GeminiService(geminiApiKey);
            
            // Build JDA instance
            JDA jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.customStatus("Coding in Java ☕"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new MessageListener(channelId, geminiService))
                    .build();
            
            jda.awaitReady();
            logger.info("Kira bot is online and ready!");
            
        } catch (Exception e) {
            logger.error("Failed to start bot", e);
            System.exit(1);
        }
    }
}