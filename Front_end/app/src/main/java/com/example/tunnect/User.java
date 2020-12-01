package com.example.tunnect;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private List<Song> songs;
    private List<String> matches;
    private List<String> dislikes;
    private List<String> likes;
    private String favGenre;

    public User(String userId, String username, List<Song> songs, List<String> matches, List<String> dislikes, List<String> likes, String favGenre) {
        this.userId = userId;
        this.username = username;
        this.songs = songs;
        this.matches = matches;
        this.likes = likes;
        this.dislikes = dislikes;
        this.favGenre = favGenre;
    }

    public User() {
        this.songs = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
    }

    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<Song> getSongs() { return songs; }

    public List<String> getMatches() {return matches; }

    public List<String> getLikes() {return likes;}

    public List<String> getDislikes() {return dislikes;}

    public void addLike(String userId) { likes.add(userId); }

    public void addDislike(String userId) {
        dislikes.add(userId);
    }

    public void addMatch(String userId) {
        matches.add(userId);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void setSongs(List<Song> songs) { this.songs = songs; }

    public void setFavGenre(String favGenre) { this.favGenre = favGenre; }

    public String getFavGenre() { return favGenre; }
}
