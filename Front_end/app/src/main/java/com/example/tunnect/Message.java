package com.example.tunnect;


/*
 * This class represents the data type for a chat message for chats between 2 users.
 */
public class Message {
    private long id;
    private String name;
    private String timestamp;
    private String colour;

    public Message(long id, String name, String timestamp, String colour) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.colour = colour;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getColour() {
        return colour;
    }
}
