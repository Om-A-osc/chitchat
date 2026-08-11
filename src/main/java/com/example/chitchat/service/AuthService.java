package com.example.chitchat.service;

import com.example.chitchat.dto.AuthRequest;
import com.example.chitchat.dto.AuthResponse;
import com.example.chitchat.entity.UserEntity;
import com.example.chitchat.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, JWTService jwtService ){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse authorizeUser(AuthRequest req){
        String username = req.getUsername();
        String password = req.getPassword();

        Optional<UserEntity> user = userRepository.findById(username);
        if( user.isEmpty() ) return null;

        UserEntity foundUser = user.get();

        if( foundUser.getPassword().equals(password) ){
            try{
                String accessToken = jwtService.generateAccessToken(username);
                String refreshToken = jwtService.generateRefreshToken(username);
                AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
                return authResponse;
            }
            catch( JOSEException j ){
                System.out.println("User authenticated but token generation failed.");
            }
        }
        return null;
    }


}
