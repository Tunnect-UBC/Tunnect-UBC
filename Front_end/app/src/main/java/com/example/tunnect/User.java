package com.example.tunnect;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private List<Song> songs;
    private List<String> matches;

    public User(String userId, String username, List<Song> songs, List<String> matches) {
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

    public void updateSongs(List<Song> songs) { this.songs = songs; }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<Song> getSongs() { return songs; }

    public List<String> getMatches() {return matches; }

    public void addSong(Song song) {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        songs.add(song);
    }
}
