package com.example.chitchat.repository;

import com.example.chitchat.entity.RoomKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomKeyRepository extends JpaRepository<RoomKeyEntity, UUID> {
    Optional<RoomKeyEntity> findByRoomIdAndUsername(UUID roomId, String username);
    List<RoomKeyEntity> findByRoomId(UUID roomId);
}
