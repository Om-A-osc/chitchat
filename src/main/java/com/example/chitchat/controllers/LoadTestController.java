package com.example.chitchat.controllers;

import com.example.chitchat.dto.LoadTestMessageRequest;
import com.example.chitchat.entity.LoadTestMessage;
import com.example.chitchat.repository.LoadTestMessageRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class LoadTestController {

    private final LoadTestMessageRepository loadTestMessageRepository;

    public LoadTestController(LoadTestMessageRepository loadTestMessageRepository) {
        this.loadTestMessageRepository = loadTestMessageRepository;
    }

    @PostMapping("/message")
    public ResponseEntity<?> postMessage(@RequestBody LoadTestMessageRequest request) {
        if (request.getClientName() == null || request.getMsg() == null) {
            return ResponseEntity.badRequest().body("client-name and msg are required");
        }
        UUID id = UUID.randomUUID();
        LoadTestMessage message = new LoadTestMessage(
                id,
                request.getClientName(),
                request.getMsg(),
                LocalDateTime.now()
        );
        try {
            loadTestMessageRepository.save(message);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate message ID");
        }
        return ResponseEntity.ok(Map.of("id", id.toString(), "status", "stored"));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<LoadTestMessage>> getFeed() {
        List<LoadTestMessage> messages = loadTestMessageRepository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(messages);
    }
}
