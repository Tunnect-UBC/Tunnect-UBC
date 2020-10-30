package com.example.tunnect;

/*
 * This class represents the data type for a chat that the current user has with another.
 * It contains the id, name, and icon colour of the other user. It also contains the last
 * message that was sent between the 2 users.
 */
public class Chat {
    private String id;
    private String name;
    private String lastMessage;
    private String timestamp;
    private int colour;

    public Chat(String id, String name, String lastMessage, String timestamp, int colour) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.colour = colour;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getColour() {
        return colour;
    }
}
