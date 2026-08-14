package com.example.chitchat.controllers;

import com.example.chitchat.dto.CreateUserRequest;
import com.example.chitchat.entity.UserEntity;
import com.example.chitchat.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class CreateNewUserController {

    private final UserService userService;

    public CreateNewUserController( UserService userService ){
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public UserEntity createUser(@RequestBody CreateUserRequest req){
        return userService.createUser(req);
    }

    @PostMapping("/user/public-key")
    public void updatePublicKey(@RequestBody Map<String, String> body, Authentication authentication) {
        String username = authentication.getName();
        String publicKey = body.get("publicKey");
        userService.updatePublicKey(username, publicKey);
    }

    @GetMapping("/user/{username}/public-key")
    public Map<String, String> getPublicKey(@PathVariable String username) {
        String key = userService.getPublicKey(username);
        return Map.of("username", username, "publicKey", key != null ? key : "");
    }

}
