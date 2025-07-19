package com.kira.bot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service for interacting with Google Gemini API
 */
@Slf4j
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    
    public GeminiService(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    
    public String generateResponse(String prompt) throws IOException {
        JsonObject requestBody = buildRequestBody(prompt);
        
        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
                ))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-goog-api-key", apiKey)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Gemini API error: {} - {}", response.code(), response.message());
                throw new IOException("Gemini API request failed: " + response.code());
            }
            
            String responseBody = response.body().string();
            return parseResponse(responseBody);
        }
    }
    
    private JsonObject buildRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);
        
        // Add generation config for better responses
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 1024);
        requestBody.add("generationConfig", generationConfig);
        
        return requestBody;
    }
    
    private String parseResponse(String responseBody) {
        try {
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            if (jsonResponse.has("candidates")) {
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates.size() > 0) {
                    JsonObject candidate = candidates.get(0).getAsJsonObject();
                    if (candidate.has("content")) {
                        JsonObject content = candidate.getAsJsonObject("content");
                        if (content.has("parts")) {
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                JsonObject part = parts.get(0).getAsJsonObject();
                                if (part.has("text")) {
                                    return part.get("text").getAsString().trim();
                                }
                            }
                        }
                    }
                }
            }
            
            log.warn("Unexpected response format: {}", responseBody);
            return "Что-то пошло не так с API. Проверь логи.";
            
        } catch (Exception e) {
            log.error("Error parsing Gemini response", e);
            return "Ошибка парсинга ответа. Типичная проблема с JSON.";
        }
    }
}