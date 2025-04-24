package com.ovenAI.Tusk.controller;

import com.ovenAI.Tusk.model.HistoryNote;
import com.ovenAI.Tusk.model.User;
import com.ovenAI.Tusk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = {"http://localhost:3000", "https://darling-blatantly-panther.ngrok-free.app"}, allowCredentials = "true")
public class SessionController {

    @Autowired
    UserService userService;

    SessionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-chat")
    public ResponseEntity<?> startNewSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.getSavedData().add(new ArrayList<>());

        userService.updateUser(currentUser);
        
        return ResponseEntity.ok("New session created.");
    }

    @GetMapping("/getChat")
    public ResponseEntity<?> getChatSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(currentUser.getSavedData());
    }
}
