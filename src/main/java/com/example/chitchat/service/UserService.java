package com.example.chitchat.service;

import com.example.chitchat.dto.CreateUserRequest;
import com.example.chitchat.entity.UserEntity;
import com.example.chitchat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(CreateUserRequest req){
        UserEntity user = new UserEntity();

        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setTagline(req.getTagline());
        user.setProfilePicture(req.getProfilePicture());
        user.setPublicKey(req.getPublicKey());
        user.setTimestamp(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void updatePublicKey(String username, String publicKey) {
        userRepository.findById(username).ifPresent(user -> {
            user.setPublicKey(publicKey);
            userRepository.save(user);
        });
    }

    public String getPublicKey(String username) {
        return userRepository.findById(username)
                .map(UserEntity::getPublicKey)
                .orElse(null);
    }

    public boolean userExists(String username){
        return userRepository.findById(username).isPresent();
    }



}
