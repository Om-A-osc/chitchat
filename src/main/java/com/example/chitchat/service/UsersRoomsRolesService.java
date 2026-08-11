package com.example.chitchat.service;

import com.example.chitchat.entity.UserEntity;
import com.example.chitchat.entity.UsersRoomsRolesEntity;
import com.example.chitchat.repository.UsersRoomsRolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersRoomsRolesService {
    UsersRoomsRolesRepository usersRoomsRolesRepository;
    public UsersRoomsRolesService( UsersRoomsRolesRepository usersRoomsRolesRepository ){
        this.usersRoomsRolesRepository = usersRoomsRolesRepository;
    }
    public String createUsersRoomsRoles(List<UsersRoomsRolesEntity> usersRoomsRoles){
        for( UsersRoomsRolesEntity urr : usersRoomsRoles ){
            usersRoomsRolesRepository.save(urr);
        }
        return "users_rooms_roles populated";
    }
}
