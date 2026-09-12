package com.example.chitchat.repository;

import com.example.chitchat.entity.LoadTestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoadTestMessageRepository extends JpaRepository<LoadTestMessage, UUID> {
    List<LoadTestMessage> findAllByOrderByIdAsc();
}
