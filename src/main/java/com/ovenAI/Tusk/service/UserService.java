package com.ovenAI.Tusk.service;

import com.ovenAI.Tusk.model.User;
import com.ovenAI.Tusk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("User Already Exist");
            return false;
        }
        userRepository.save(user);
        System.out.println("Successful");
        return true;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteChat(User user, int index) {
        try {
            if (index >= 0 && index < user.getSavedData().size()) {
                user.getSavedData().remove(index);
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Invalid index: " + index);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete chat: " + e.getMessage(), e);
        }
    }
}