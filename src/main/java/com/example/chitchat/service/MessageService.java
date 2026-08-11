package com.example.chitchat.service;

import com.example.chitchat.dto.ChatMessageRequest;
import com.example.chitchat.entity.MessageEntity;
import com.example.chitchat.entity.UsersRoomsRolesEntity;
import com.example.chitchat.repository.MessageRepository;
import com.example.chitchat.repository.UsersRoomsRolesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UsersRoomsRolesRepository usersRoomsRolesRepository;
    private final RoomService roomService;

    public MessageService(MessageRepository messageRepository,
                          UsersRoomsRolesRepository usersRoomsRolesRepository, RoomService roomService){
        this.messageRepository = messageRepository;
        this.usersRoomsRolesRepository = usersRoomsRolesRepository;
        this.roomService = roomService;
    }


    public MessageEntity saveMessage(ChatMessageRequest req, String username){
        UUID roomId = req.getRoomId();
        String content = req.getContent();

        if(!roomService.isUserMember(username,roomId)){
            throw new RuntimeException("User not a member of room");
        }

        MessageEntity messageEntity = new MessageEntity(roomId, username, content, LocalDateTime.now(), null , false);

        return messageRepository.save(messageEntity);
    }



}
