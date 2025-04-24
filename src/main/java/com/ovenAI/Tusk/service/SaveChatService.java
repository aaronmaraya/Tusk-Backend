package com.ovenAI.Tusk.service;

import com.ovenAI.Tusk.model.HistoryNote;
import com.ovenAI.Tusk.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SaveChatService {
    @Autowired
    private final UserService userService;
    @Autowired
    ResponseService responseService;
    @Getter
    private List<HistoryNote> latestHistory;

    SaveChatService(UserService userService, ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }

    public void saveChat(HttpSession session, String question, Map<String, Object> requestBody, String title) {
        String answerText = responseService.getContentAnswerText(requestBody);
        String username = (String) session.getAttribute("username");

        User currentUser = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        HistoryNote historyNote = new HistoryNote(question, answerText, title);

        latestHistory = currentUser.getSavedData().get(currentUser.getSavedData().size() - 1);
        latestHistory.add(historyNote);

        userService.updateUser(currentUser);
    }
}
