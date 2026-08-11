package com.example.chitchat.controllers;

import com.example.chitchat.dto.AuthRequest;
import com.example.chitchat.dto.AuthResponse;
import com.example.chitchat.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public AuthResponse authorizeUser(@RequestBody AuthRequest req){
        return authService.authorizeUser(req);
    }


}
