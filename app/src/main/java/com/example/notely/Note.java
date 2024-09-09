package com.example.notely;

public class Note {
    private String title;
    private String description;
    private String timestamp;

    // Empty constructor for Firebase
    public Note() {}

    public Note(String title, String description, String timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
