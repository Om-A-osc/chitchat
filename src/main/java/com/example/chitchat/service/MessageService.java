package com.example.chitchat.service;

import com.example.chitchat.dto.ChatMessageRequest;
import com.example.chitchat.dto.ChatMessageResponse;
import com.example.chitchat.dto.MessageDeliveryStatus;
import com.example.chitchat.entity.MessageEntity;
import com.example.chitchat.entity.MessageReceipt;
import com.example.chitchat.entity.MessageReceiptId;
import com.example.chitchat.entity.UsersRoomsRolesEntity;
import com.example.chitchat.repository.MessageReceiptRepository;
import com.example.chitchat.repository.MessageRepository;
import com.example.chitchat.repository.UsersRoomsRolesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UsersRoomsRolesRepository usersRoomsRolesRepository;
    private final RoomService roomService;
    private final MessageReceiptRepository messageReceiptRepository;

    public MessageService(MessageRepository messageRepository,
                          UsersRoomsRolesRepository usersRoomsRolesRepository,
                          RoomService roomService,
                          MessageReceiptRepository messageReceiptRepository){
        this.messageRepository = messageRepository;
        this.usersRoomsRolesRepository = usersRoomsRolesRepository;
        this.roomService = roomService;
        this.messageReceiptRepository = messageReceiptRepository;
    }


    public MessageEntity saveMessage(ChatMessageRequest req, String username){
        UUID roomId = req.getRoomId();
        String content = req.getContent();

        if(!roomService.isUserMember(username,roomId)){
            throw new RuntimeException("User not a member of room");
        }

        MessageEntity messageEntity = new MessageEntity(roomId, username, content, LocalDateTime.now(), null , false);

        MessageEntity saveMessageEntity = messageRepository.save(messageEntity);
        initializeMessageReceipt(roomId, saveMessageEntity.getMessageId());
        return saveMessageEntity;
    }

    public void initializeMessageReceipt(UUID roomId , UUID messageId ){
        List<String> userNames = usersRoomsRolesRepository.findUsernameByRoomId(roomId);

        for( String u : userNames ){
            messageReceiptRepository.save(new MessageReceipt( new MessageReceiptId(messageId, u), null, null) );
        }
    }

    public List<MessageEntity> getRecentMessages(UUID roomId) {

        LocalDateTime cutoff =
                LocalDateTime.now().minusDays(2);

        return messageRepository.findRecentMessages(
                roomId,
                cutoff
        );
    }

    public void updateMessageReceiptDeliveredStatus(MessageDeliveryStatus status, String username){
        UUID messageId = status.getMessageId();

        MessageEntity messageEntity = messageRepository.findById(messageId).orElse(null);
        UsersRoomsRolesEntity foundUsersRoomsRolesEntity = usersRoomsRolesRepository.findByIdUsernameAndIdRoomId(username,status.getRoomId()).orElse(null);

        if( messageEntity==null || foundUsersRoomsRolesEntity==null || !messageEntity.getRoomId().equals(status.getRoomId()) ){
            return;
        }

        MessageReceiptId id = new MessageReceiptId(messageId,username);
        MessageReceipt messageReceipt = messageReceiptRepository.findById(id).orElse(null);
        if(messageReceipt==null) return;
        messageReceipt.setMessageDelivered(LocalDateTime.now());
        messageReceiptRepository.save(messageReceipt);
    }

    public void updateMessageReceiptReadStatus(MessageDeliveryStatus status, String username){
        UUID messageId = status.getMessageId();

        MessageEntity messageEntity = messageRepository.findById(messageId).orElse(null);
        UsersRoomsRolesEntity foundUsersRoomsRolesEntity = usersRoomsRolesRepository.findByIdUsernameAndIdRoomId(username,status.getRoomId()).orElse(null);

        if( messageEntity==null || foundUsersRoomsRolesEntity==null || !messageEntity.getRoomId().equals(status.getRoomId()) ){
            return;
        }

        MessageReceiptId id = new MessageReceiptId(messageId,username);
        MessageReceipt messageReceipt = messageReceiptRepository.findById(id).orElse(null);
        if(messageReceipt==null) return;
        messageReceipt.setMessageRead(LocalDateTime.now());
        messageReceiptRepository.save(messageReceipt);
    }


    public List<MessageReceipt> getMessageReceipt(UUID messageId , String username){
        MessageEntity messageEntity = messageRepository.findById(messageId).orElse(null);
        if( messageEntity==null ) return null;
        UUID roomId = messageEntity.getRoomId();
        UsersRoomsRolesEntity u = usersRoomsRolesRepository.findByIdUsernameAndIdRoomId(username,roomId).orElse(null);
        if(u==null) return null;
        List<MessageReceipt> messageReceipts = messageReceiptRepository.findAllByMessageId(messageId);
        return messageReceipts;
    }
}
