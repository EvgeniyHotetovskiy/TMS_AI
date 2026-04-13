package org.ai_agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MistralClient {

    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String API_KEY = System.getenv("MISTRAL_API_KEY");
    private static final ObjectMapper mapper = new ObjectMapper(); // Создаем один раз

    public static String call(String prompt) {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new RuntimeException("MISTRAL_API_KEY is not set in environment variables");
        }

        try {
            // 1. Формируем JSON через объектную модель Jackson
            // Это гарантирует 100% корректное экранирование любых символов
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("model", "mistral-small-latest");
            rootNode.put("temperature", 0.2);

            ArrayNode messages = rootNode.putArray("messages");

            // System message
            ObjectNode systemMsg = messages.addObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "You are a QA automation engineer. Return structured output.");

            // User message
            ObjectNode userMsg = messages.addObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt); // Передаем чистый prompt, Jackson сам его экранирует

            String jsonBody = mapper.writeValueAsString(rootNode);

            // 2. Отправляем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json") // Хорошая практика добавить
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 3. Проверяем HTTP статус (если не 200, лучше знать об этом сразу)
            if (response.statusCode() != 200) {
                System.err.println("API Error. Status: " + response.statusCode());
                System.err.println("Response Body: " + response.body());
            }

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Error during Mistral API call", e);
        }
    }
}