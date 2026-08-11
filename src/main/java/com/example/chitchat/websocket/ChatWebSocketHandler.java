package com.example.chitchat.websocket;

import com.example.chitchat.dto.ChatMessageRequest;
import com.example.chitchat.dto.ChatMessageResponse;
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

        ChatMessageRequest req = objectMapper.readValue(message.getPayload(),ChatMessageRequest.class);

        if("SEND_MESSAGE".equals(req.getType())){
            MessageEntity savedMessage = messageService.saveMessage(req,username);
            ChatMessageResponse messageToSend = new ChatMessageResponse();

            messageToSend.setMessageId(savedMessage.getMessageId());
            messageToSend.setContent(savedMessage.getContent());
            messageToSend.setCreatedTimestamp(savedMessage.getCreatedTimestamp());

            webSocketSessionManager.broadcastToRoom(req.getRoomId(),messageToSend);
        }
        else if("JOIN_ROOM".equals(req.getType())){
            UUID roomId = req.getRoomId();
            if( !roomService.isUserMember(username,roomId) ) return;
            webSocketSessionManager.joinRoom(req.getRoomId(),session);
        }
        else if("LEAVE_ROOM".equals(req.getType())){
            webSocketSessionManager.leaveRoom(req.getRoomId(),session);
        }

    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        System.out.println("Connection closed " + session.getId());
    }
}
