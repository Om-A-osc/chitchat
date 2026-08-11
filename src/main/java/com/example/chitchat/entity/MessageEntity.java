package com.example.chitchat.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageId;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdTimestamp;

    private LocalDateTime lastEditedTimestamp;

    private boolean isDeleted = false;

    public MessageEntity(UUID roomId, String username, String content, LocalDateTime createdTimestamp, LocalDateTime lastEditedTimestamp, boolean isDeleted){
        this.roomId = roomId;
        this.username = username;
        this.content = content;
        this.createdTimestamp=createdTimestamp;
        this.lastEditedTimestamp = lastEditedTimestamp;
        this.isDeleted = isDeleted;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalDateTime getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }

    public void setLastEditedTimestamp(LocalDateTime lastEditedTimestamp) {
        this.lastEditedTimestamp = lastEditedTimestamp;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
