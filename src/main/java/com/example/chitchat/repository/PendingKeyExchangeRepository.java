package com.example.chitchat.repository;

import com.example.chitchat.entity.PendingKeyExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface PendingKeyExchangeRepository extends JpaRepository<PendingKeyExchangeEntity, UUID> {
    List<PendingKeyExchangeEntity> findByRecipientUsername(String recipientUsername);
    
    @Transactional
    @Modifying
    void deleteByRecipientUsernameAndRoomId(String recipientUsername, UUID roomId);
}
