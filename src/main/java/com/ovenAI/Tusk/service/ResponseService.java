package com.ovenAI.Tusk.service;

import com.ovenAI.Tusk.components.ExtractAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ResponseService {
    private final RestClient restClient;
    private final String apiKey;

    @Autowired
    private final ExtractAnswer extractAnswer;
    ResponseService(RestClient.Builder restClientBuilder,
                    @Value("${spring.ai.openai.api-key}") String apiKey,
                    ExtractAnswer extractAnswer) {
        this.restClient = restClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
        this.apiKey = apiKey;
        this.extractAnswer = extractAnswer;
    }

    public Map<String, Object> generateContent(Map<String, Object> requestBody) {
        return restClient.post()
                .uri("/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public String getContentAnswerText(Map<String, Object> requestBody) {
        return extractAnswer.extractAnswerText(generateContent(requestBody));
    }
}
