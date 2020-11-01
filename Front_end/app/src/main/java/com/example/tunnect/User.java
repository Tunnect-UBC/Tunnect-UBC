package com.example.tunnect;

public class User {
    private String userId;
    private String username;
    private String top_artist;

    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateTopArtist(String top_artist) {
        this.top_artist = top_artist;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getTopArtist() {
        return top_artist;
    }
}
