package com.example.chitchat.controllers;

import com.example.chitchat.dto.LoadTestMessageRequest;
import com.example.chitchat.entity.LoadTestMessage;
import com.example.chitchat.repository.LoadTestMessageRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class LoadTestController {

    private final LoadTestMessageRepository loadTestMessageRepository;
    private final JdbcTemplate jdbcTemplate;

    public LoadTestController(LoadTestMessageRepository loadTestMessageRepository, JdbcTemplate jdbcTemplate) {
        this.loadTestMessageRepository = loadTestMessageRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/message")
    public ResponseEntity<?> postMessage(@RequestBody LoadTestMessageRequest request) {
        if (request.getClientName() == null || request.getMsg() == null) {
            return ResponseEntity.badRequest().body("client-name and msg are required");
        }
        UUID id = UUID.randomUUID();
        try {
            // Fast path: single raw INSERT, bypassing JPA merge (extra SELECT)
            // and persistence-context overhead on the hot path.
            jdbcTemplate.update(
                    "INSERT INTO load_test_messages(id, client_name, msg, \"timestamp\") VALUES (?,?,?,?)",
                    id, request.getClientName(), request.getMsg(), LocalDateTime.now());
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
