package com.example.chitchat.config;

import com.example.chitchat.service.JWTService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JWTHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTService jwtService;
    public JWTHandshakeInterceptor( JWTService jwtService ){
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        if( query==null ) return false;
        String accessToken = null;

        for( String parameter : query.split("&") ){
            String [] pair = parameter.split("=");
            if( pair.length==2 && pair[0].equals("token")){
                accessToken = pair[1];
                break;
            }
        }

        if( accessToken==null ) return false;

        try{
            boolean validToken = jwtService.validateAccessToken(accessToken);
            if( !validToken ) return false;
            String username = jwtService.getUsernameFromToken(accessToken);
            attributes.put("username", username);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
