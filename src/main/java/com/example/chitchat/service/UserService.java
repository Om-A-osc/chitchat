package com.example.chitchat.service;

import com.example.chitchat.dto.CreateUserRequest;
import com.example.chitchat.entity.UserEntity;
import com.example.chitchat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserEntity createUser(CreateUserRequest req){
        UserEntity user = new UserEntity();

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setTagline(req.getTagline());
        user.setProfilePicture(req.getProfilePicture());
        user.setTimestamp(LocalDateTime.now());

        return userRepository.save(user);
    }

    public boolean userExists(String username){
        return userRepository.existsById(username);
    }



}
