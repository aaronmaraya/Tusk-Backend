package com.ovenAI.Tusk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HistoryNote {
    private String question;
    private String answer;
    private String title;  // Added title field
    private Date timestamp;

    public HistoryNote() {}

    public HistoryNote(String question, String answer, String title) {
        this.question = question;
        this.answer = answer;
        this.title = title;
        this.timestamp = new Date();
    }
}
