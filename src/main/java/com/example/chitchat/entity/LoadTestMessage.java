package com.example.chitchat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "load_test_messages")
public class LoadTestMessage {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String msg;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LoadTestMessage() {
    }

    public LoadTestMessage(UUID id, String clientName, String msg, LocalDateTime timestamp) {
        this.id = id;
        this.clientName = clientName;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
