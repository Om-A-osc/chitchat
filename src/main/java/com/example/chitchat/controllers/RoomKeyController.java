package com.example.chitchat.controllers;

import com.example.chitchat.service.RoomKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class RoomKeyController {

    private final RoomKeyService roomKeyService;

    public RoomKeyController(RoomKeyService roomKeyService) {
        this.roomKeyService = roomKeyService;
    }

    @PostMapping("/room/{roomId}/keys")
    public ResponseEntity<Void> saveRoomKeys(
            @PathVariable UUID roomId,
            @RequestBody Map<String, String> userEncryptedKeys) {
        roomKeyService.saveRoomKeys(roomId, userEncryptedKeys);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/key")
    public ResponseEntity<Map<String, String>> getRoomKey(
            @PathVariable UUID roomId,
            Authentication authentication) {
        String username = authentication.getName();
        String encryptedKey = roomKeyService.getRoomKeyForUser(roomId, username);
        if (encryptedKey == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(Map.of("roomId", roomId.toString(), "encryptedKey", encryptedKey));
    }
}
