package com.ovenAI.Tusk.controller;

import com.ovenAI.Tusk.components.ExtractAnswer;
import com.ovenAI.Tusk.model.HistoryNote;
import com.ovenAI.Tusk.model.User;
import com.ovenAI.Tusk.service.GenerateTitleService;
import com.ovenAI.Tusk.service.ResponseService;
import com.ovenAI.Tusk.service.SaveChatService;
import com.ovenAI.Tusk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tusk")
@CrossOrigin(origins = {"http://localhost:3000", "https://darling-blatantly-panther.ngrok-free.app"}, allowCredentials = "true")
public class GeminiModelController {
    @Autowired
    private final ResponseService responseService;
    @Autowired
    private final SaveChatService saveChatService;
    @Autowired
    private final GenerateTitleService generateTitleService;

    public GeminiModelController(SaveChatService saveChatService,
                                 ResponseService responseService,
                                 GenerateTitleService generateTitleService) {
        this.responseService = responseService;
        this.saveChatService = saveChatService;
        this.generateTitleService = generateTitleService;
    }


    @PostMapping(value = "/ask-pdf")
    public ResponseEntity<?> analyzePdf(@RequestParam("file") MultipartFile file,
                                        @RequestParam("question") String question,
                                        HttpSession session) throws IOException {

        String base64Data = Base64.getEncoder().encodeToString(file.getBytes());

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("text", "You are a PDF analysis expert. Only respond based on the document content. Use list if needed. " +
                                        "If the answer cannot be found, say: 'The answer is not in the document.'\n\n" +
                                        "Question: " + question),
                                Map.of("inlineData", Map.of(
                                        "mimeType", "application/pdf",
                                        "data", base64Data
                                ))
                        )
                ))
        );

        String title = generateTitleService.generateTitle(question);

        saveChatService.saveChat(session, question, requestBody, title);

        Map<String, Object> responseBody = Map.of(
                "title", title,
                "answer", responseService.getContentAnswerText(requestBody),
                "chatHistory", saveChatService.getLatestHistory()
        );

        return ResponseEntity.ok(responseBody);
    }

}
