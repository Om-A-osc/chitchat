package com.example.chitchat.websocket;

import com.example.chitchat.dto.ChatMessageRequest;
import com.example.chitchat.dto.ChatMessageResponse;
import com.example.chitchat.dto.MessageDeliveryStatus;
import com.example.chitchat.dto.WebSocketBaseRequest;
import com.example.chitchat.entity.MessageEntity;
import com.example.chitchat.service.MessageService;
import com.example.chitchat.service.RoomService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebsocketSessionManager webSocketSessionManager;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    private final RoomService roomService;
    private final com.example.chitchat.repository.PendingKeyExchangeRepository pendingKeyExchangeRepository;

    public ChatWebSocketHandler(WebsocketSessionManager webSocketSessionManager,
                                ObjectMapper objectMapper,
                                MessageService messageService,
                                RoomService roomService,
                                com.example.chitchat.repository.PendingKeyExchangeRepository pendingKeyExchangeRepository){
        this.webSocketSessionManager = webSocketSessionManager;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
        this.roomService = roomService;
        this.pendingKeyExchangeRepository = pendingKeyExchangeRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("Connection received " + session.getId());
        String username = (String) session.getAttributes().get("username");
        System.out.println("Connected username " + username);

        // Send any pending key exchanges to the user
        if (username != null) {
            java.util.List<com.example.chitchat.entity.PendingKeyExchangeEntity> pendingExchanges = 
                pendingKeyExchangeRepository.findByRecipientUsername(username);
            for (com.example.chitchat.entity.PendingKeyExchangeEntity exchange : pendingExchanges) {
                com.example.chitchat.dto.KeyExchangeRequest req = new com.example.chitchat.dto.KeyExchangeRequest();
                req.setType("KEY_EXCHANGE");
                req.setRoomId(exchange.getRoomId());
                req.setSenderUsername(exchange.getSenderUsername());
                req.setRecipientUsername(exchange.getRecipientUsername());
                req.setEncryptedKey(exchange.getEncryptedKey());
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(req)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String username = (String) session.getAttributes().get("username");

        WebSocketBaseRequest req = objectMapper.readValue(message.getPayload(), WebSocketBaseRequest.class);

        if("SEND_MESSAGE".equals(req.getType())){
            ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
            MessageEntity savedMessage = messageService.saveMessage(request,username);
            ChatMessageResponse messageToSend = new ChatMessageResponse();
            messageToSend.setMessageId(savedMessage.getMessageId());
            messageToSend.setContent(savedMessage.getContent());
            messageToSend.setCreatedTimestamp(savedMessage.getCreatedTimestamp());
            messageToSend.setSender(username);
            messageToSend.setRoomId(savedMessage.getRoomId());
            messageToSend.setType("CHAT_MESSAGE");
            webSocketSessionManager.broadcastToRoom(request.getRoomId(),messageToSend);
        }
        else if("MESSAGE_DELIVERED".equals(req.getType())){
            MessageDeliveryStatus request = objectMapper.readValue(message.getPayload(),MessageDeliveryStatus.class);
            messageService.updateMessageReceiptDeliveredStatus(request,username);
            webSocketSessionManager.broadcastToRoomMessageStatus(request, username);
        }
        else if("MESSAGE_READ".equals(req.getType())){
            MessageDeliveryStatus request = objectMapper.readValue(message.getPayload(),MessageDeliveryStatus.class);
            messageService.updateMessageReceiptReadStatus(request,username);
            webSocketSessionManager.broadcastToRoomMessageStatus(request, username);
        }
        else if("JOIN_ROOM".equals(req.getType())){
            ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
            UUID roomId = request.getRoomId();
            if( !roomService.isUserMember(username,roomId) ) return;
            webSocketSessionManager.joinRoom(request.getRoomId(),session);
        }
        else if("REQUEST_ROOM_KEY".equals(req.getType())){
            ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
            UUID roomId = request.getRoomId();
            if(!roomService.isUserMember(username, roomId)) return;
            // Broadcast the request to the room. The sender is set to the requesting user.
            ChatMessageResponse reqResponse = new ChatMessageResponse();
            reqResponse.setType("REQUEST_ROOM_KEY");
            reqResponse.setRoomId(roomId);
            reqResponse.setSender(username);
            webSocketSessionManager.broadcastToRoom(roomId, reqResponse);
        }
        else if("KEY_EXCHANGE".equals(req.getType())){
            com.example.chitchat.dto.KeyExchangeRequest request = objectMapper.readValue(message.getPayload(), com.example.chitchat.dto.KeyExchangeRequest.class);
            UUID roomId = request.getRoomId();
            if(!roomService.isUserMember(username, roomId)) return;
            
            request.setSenderUsername(username); // ensure sender is correct
            // Save to mailbox
            pendingKeyExchangeRepository.save(new com.example.chitchat.entity.PendingKeyExchangeEntity(
                    roomId, username, request.getRecipientUsername(), request.getEncryptedKey()));
            
            // Broadcast to the room (the recipient client will filter it) or directly to the recipient session if we track them.
            // Since we only have broadcastToRoom, we'll use that.
            webSocketSessionManager.broadcastToRoom(roomId, request);
        }
        else if("ACK_KEY_EXCHANGE".equals(req.getType())){
            ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
            // The recipient acknowledges they got the key
            pendingKeyExchangeRepository.deleteByRecipientUsernameAndRoomId(username, request.getRoomId());
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        System.out.println("Connection closed " + session.getId());
    }
}
