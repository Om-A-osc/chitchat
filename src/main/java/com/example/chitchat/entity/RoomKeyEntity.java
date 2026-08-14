package com.example.chitchat.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "room_keys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"roomId", "username"})
})
public class RoomKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String encryptedKey;

    public RoomKeyEntity() {}

    public RoomKeyEntity(UUID roomId, String username, String encryptedKey) {
        this.roomId = roomId;
        this.username = username;
        this.encryptedKey = encryptedKey;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getRoomId() { return roomId; }
    public void setRoomId(UUID roomId) { this.roomId = roomId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEncryptedKey() { return encryptedKey; }
    public void setEncryptedKey(String encryptedKey) { this.encryptedKey = encryptedKey; }
}
