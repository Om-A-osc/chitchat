package com.example.chitchat.service;

import com.example.chitchat.entity.RoomKeyEntity;
import com.example.chitchat.repository.RoomKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomKeyService {

    private final RoomKeyRepository roomKeyRepository;
    private final RoomService roomService;

    public RoomKeyService(RoomKeyRepository roomKeyRepository, RoomService roomService) {
        this.roomKeyRepository = roomKeyRepository;
        this.roomService = roomService;
    }

    @Transactional
    public void saveRoomKeys(UUID roomId, Map<String, String> userEncryptedKeys) {
        if (userEncryptedKeys == null) return;
        userEncryptedKeys.forEach((user, key) -> {
            Optional<RoomKeyEntity> existing = roomKeyRepository.findByRoomIdAndUsername(roomId, user);
            if (existing.isPresent()) {
                RoomKeyEntity entity = existing.get();
                entity.setEncryptedKey(key);
                roomKeyRepository.save(entity);
            } else {
                roomKeyRepository.save(new RoomKeyEntity(roomId, user, key));
            }
        });
    }

    public String getRoomKeyForUser(UUID roomId, String username) {
        if (!roomService.isUserMember(username, roomId)) {
            return null;
        }
        return roomKeyRepository.findByRoomIdAndUsername(roomId, username)
                .map(RoomKeyEntity::getEncryptedKey)
                .orElse(null);
    }
}
