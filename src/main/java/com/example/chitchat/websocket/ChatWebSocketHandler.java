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

    public ChatWebSocketHandler(WebsocketSessionManager webSocketSessionManager,
                                ObjectMapper objectMapper,
                                MessageService messageService,
                                RoomService roomService){
        this.webSocketSessionManager = webSocketSessionManager;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
        this.roomService = roomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("Connection received " + session.getId());
        System.out.println("Connected username " + session.getAttributes().get("username"));
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


    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        String username = (String) session.getAttributes().get("username");
        webSocketSessionManager.removeSession(session);
        System.out.println("Connection closed " + session.getId());
    }
}
