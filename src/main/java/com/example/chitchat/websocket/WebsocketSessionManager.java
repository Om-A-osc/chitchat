package com.example.chitchat.websocket;

import com.example.chitchat.dto.ChatMessageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Component
public class WebsocketSessionManager {

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<UUID, Set<WebSocketSession> > roomSessions = new ConcurrentHashMap<>();

    public WebsocketSessionManager(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public void joinRoom( UUID roomId, WebSocketSession session){
        roomSessions.computeIfAbsent(roomId,
                id-> ConcurrentHashMap.newKeySet() ).add(session);
    }

    public void leaveRoom( UUID roomId, WebSocketSession session){
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if(sessions==null) return;

        sessions.remove(session);

        if(sessions.isEmpty()) roomSessions.remove(roomId);
    }

    public void broadcastToRoom(UUID roomId, ChatMessageResponse message){
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if( sessions==null ) return;
        String json = objectMapper.writeValueAsString(message);

        for( WebSocketSession s : sessions ){
            if( !s.isOpen() ) continue;
            try{
                s.sendMessage( new TextMessage(json) );
            }
            catch( IOException e ){
                System.out.println("Failed to broadcast message to session: " + s.getId());
                sessions.remove(s);
                try{
                    s.close();
                }
                catch(IOException ee){
                    System.out.println("Could not close session:" + s.getId());
                }
            }
        }
    }

}
