package com.example.chitchat.controllers;

import com.example.chitchat.entity.MessageEntity;
import com.example.chitchat.entity.MessageReceipt;
import com.example.chitchat.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {
    private final MessageService messageService;

    public MessageController( MessageService messageService ){
        this.messageService = messageService;
    }

    @GetMapping("/rooms/{roomId}/messages/recent")
    public List<MessageEntity> getRecentMessages(
            @PathVariable UUID roomId) {
        return messageService.getRecentMessages(roomId);
    }

    @GetMapping("/rooms/{messageId}")
    public List<MessageReceipt> getMessageReceipt(@PathVariable UUID messageId, Authentication authentication){
        String username = authentication.getName();
        return messageService.getMessageReceipt(messageId , username );
    }

}
