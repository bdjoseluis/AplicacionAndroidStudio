package com.example.tfg.domain;

public class MessageDTO {
    private int id;
    private String content;
    private String sender;

    public MessageDTO(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public MessageDTO(int id, String content, String sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
    }

    public MessageDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
