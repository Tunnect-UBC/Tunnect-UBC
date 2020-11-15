package com.example.tunnect;

/*
 * This class represents the data type for a chat message from a chat between 2 users.
 */
public class Message {
    private String id;
    private String name;
    private String message;
    private long timestamp;
    private int colour;

    public Message(String sender_id, String sender_name, String message, long timestamp, int colour) {
        this.id = sender_id;
        this.name = sender_name;
        this.message = message;
        this.timestamp = timestamp;
        this.colour = colour;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getColour() {
        return colour;
    }
}
