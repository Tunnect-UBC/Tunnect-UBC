package com.example.tunnect;

import java.util.List;

public class User {
    private String userId;
    private String username;
    private List<String> songs;
    private List<String> matches;

    public User(String userId, String username, List<String> songs, List<String> matches) {
        this.userId = userId;
        this.username = username;
        this.songs = songs;
        this.matches = matches;
    }

    public User() {}

    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateSongs(List<String> songs) { this.songs = songs; }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getSongs() { return songs; }

    public List<String> getMatches() {return matches; }
}
