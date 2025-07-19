package com.kira.bot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * Handles Discord message events for Kira bot
 */
@Slf4j
public class MessageListener extends ListenerAdapter {
    
    private final String targetChannelId;
    private final GeminiService geminiService;
    private final KiraPersonality personality;
    private final CommandHandler commandHandler;
    
    public MessageListener(String targetChannelId, GeminiService geminiService) {
        this.targetChannelId = targetChannelId;
        this.geminiService = geminiService;
        this.personality = new KiraPersonality();
        this.commandHandler = new CommandHandler();
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || 
            !event.getChannel().getId().equals(targetChannelId)) {
            return;
        }
        
        String message = event.getMessage().getContentRaw().trim();
        if (message.isEmpty()) {
            return;
        }
        
        if (commandHandler.handleCommand(event, message)) {
            return;
        }
        
        boolean mentioned = event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser());
        boolean shouldRespond = personality.shouldRespond(message) || mentioned;
        
        if (!shouldRespond && !personality.isSpamMessage(message)) {
            if (Math.random() < 0.7) {
                shouldRespond = true;
            }
        }
        
        if (!shouldRespond) {
            return;
        }
        
        event.getChannel().sendTyping().queue();
        
        CompletableFuture.supplyAsync(() -> {
            try {
                String prompt = personality.buildPrompt(message, event.getAuthor().getName());
                return geminiService.generateResponse(prompt);
            } catch (Exception e) {
                log.error("Error generating response for user {}: {}", 
                           event.getAuthor().getName(), e.getMessage());
                return personality.getErrorResponse();
            }
        }).thenAccept(response -> {
            if (response != null && !response.trim().isEmpty()) {
                if (response.length() > 2000) {
                    String[] parts = splitMessage(response, 2000);
                    for (String part : parts) {
                        event.getChannel().sendMessage(part).queue();
                    }
                } else {
                    event.getChannel().sendMessage(response).queue();
                }
                log.info("Responded to {} in channel {}", 
                          event.getAuthor().getName(), event.getChannel().getName());
            }
        }).exceptionally(throwable -> {
            log.error("Error sending response", throwable);
            event.getChannel().sendMessage(personality.getErrorResponse()).queue();
            return null;
        });
    }
    
    private String[] splitMessage(String message, int maxLength) {
        if (message.length() <= maxLength) {
            return new String[]{message};
        }
        
        String[] sentences = message.split("\\. ");
        StringBuilder current = new StringBuilder();
        java.util.List<String> parts = new java.util.ArrayList<>();
        
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            if (i < sentences.length - 1) sentence += ". ";
            
            if (current.length() + sentence.length() > maxLength) {
                if (current.length() > 0) {
                    parts.add(current.toString());
                    current = new StringBuilder();
                }
                
                if (sentence.length() > maxLength) {
                    String[] words = sentence.split(" ");
                    for (String word : words) {
                        if (current.length() + word.length() + 1 > maxLength) {
                            parts.add(current.toString());
                            current = new StringBuilder(word);
                        } else {
                            if (current.length() > 0) current.append(" ");
                            current.append(word);
                        }
                    }
                } else {
                    current.append(sentence);
                }
            } else {
                current.append(sentence);
            }
        }
        
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        
        return parts.toArray(new String[0]);
    }
}