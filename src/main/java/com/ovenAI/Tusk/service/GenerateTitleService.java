package com.ovenAI.Tusk.service;

import com.ovenAI.Tusk.components.ExtractAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GenerateTitleService {
    @Autowired
    private final ExtractAnswer extractAnswer;
    @Autowired
    private final ResponseService responseService;
    GenerateTitleService(ExtractAnswer extractAnswer, ResponseService responseService) {
        this.extractAnswer = extractAnswer;
        this.responseService = responseService;
    }

    public String generateTitle(String question) {
        Map<String, Object> titleRequestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("text", "Generate a very short concise title (3-5 words max) for the following question about a document. " +
                                        "The title should summarize the question's intent. Only respond with the title, nothing else.\n\n" +
                                        "Question: " + question)
                        )
                ))
        );
        return extractAnswer.extractAnswerText(responseService.generateContent(titleRequestBody));
    }
}
