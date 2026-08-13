package com.example.chitchat.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

import java.util.UUID;

@Embeddable
public class MessageReceiptId {
    private UUID messageId;
    private String username;
    public MessageReceiptId(){};
    public MessageReceiptId(UUID messageId , String username ){
        this.messageId = messageId;
        this.username = username;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
