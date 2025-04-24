package com.ovenAI.Tusk.controller;

import com.ovenAI.Tusk.model.User;
import com.ovenAI.Tusk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "https://darling-blatantly-panther.ngrok-free.app"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.registerUser(user)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Registration successful!"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "Username already exists!"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        String username = user.getUsername();
        String password = user.getPassword();

        User foundUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!foundUser.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        session.setAttribute("username", username);

        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/userHome")
    public ResponseEntity<?> getUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not logged in"));
        }

        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok().body(Map.of(
                "username", user.getUsername()
        ));
    }

    @DeleteMapping("/deleteChat")
    public ResponseEntity<?> deleteChatIndex(@RequestBody Map<String, Integer> payload, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not logged in"));
        }
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        int index = payload.get("index");
        userService.deleteChat(user, index);

        return ResponseEntity.ok().body("Deletion Successful!");
    }

}
