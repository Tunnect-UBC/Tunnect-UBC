package com.example.tunnect;

import java.util.List;

public class User {
    private String userId;
    private String username;
    private String top_artist;
    private List<Song> songs;

    public User(String userId, String username, String top_artist, List<Song> songs) {
        this.userId = userId;
        this.username = username;
        this.top_artist = top_artist;
        this.songs = songs;
    }

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

    public List<Song> getSongs() { return songs; }
}
