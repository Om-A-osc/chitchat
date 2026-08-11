package com.example.chitchat.controllers;

import com.example.chitchat.dto.CreateRoomRequest;
import com.example.chitchat.service.RoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class RoomController {

    public RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/room/create")
    public String createRoom(@RequestBody CreateRoomRequest req, Authentication authentication){
        String username = authentication.getName();
        return roomService.createRoom(req, username);
    }
}
