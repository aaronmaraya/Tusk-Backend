package com.ovenAI.Tusk.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<List<HistoryNote>> savedData = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
