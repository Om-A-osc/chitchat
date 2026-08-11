package com.example.chitchat.service;

import com.example.chitchat.dto.CreateRoomRequest;
import com.example.chitchat.entity.RoomEntity;
import com.example.chitchat.entity.UsersRoomsRolesEntity;
import com.example.chitchat.entity.UsersRoomsRolesIdEntity;
import com.example.chitchat.repository.RoomRepository;
import com.example.chitchat.repository.UsersRoomsRolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class RoomService {
    private RoomRepository roomRepository;
    private UsersRoomsRolesRepository usersRoomsRolesRepository;
    private UserService userService;

    public RoomService(RoomRepository roomRepository, UsersRoomsRolesRepository usersRoomsRolesRepository, UserService userService){
        this.roomRepository = roomRepository;
        this.usersRoomsRolesRepository = usersRoomsRolesRepository;
        this.userService = userService;
    }

    @Transactional
    public String createRoom(CreateRoomRequest req, String username){
        Set<String> users = req.getParticipants();

        for( String user : users ){
            if( !userService.userExists(user) ){
                // group cannot be created with non-existing user
                return null;
            }
        }

        RoomEntity room = new RoomEntity();

        room.setRoomname(req.getRoomname());
        room.setCreatedTimestamp(LocalDateTime.now());
        room.setMaximumCapacity(req.getMaximumCapacity());

        roomRepository.save(room);

        UUID roomId = room.getRoomId();

        // put participants in usersRoomsRoles table
        List<UsersRoomsRolesEntity> usersRoomsRoles = new ArrayList<>();
        for( String user : users ){

            // creating composite id of roomId and username
            UsersRoomsRolesIdEntity id = new UsersRoomsRolesIdEntity(user, roomId);
            UsersRoomsRolesEntity e = new UsersRoomsRolesEntity(id,"MEMBER");

            usersRoomsRoles.add(e);
        }
        // create current user as admin
        UsersRoomsRolesIdEntity adminUserId = new UsersRoomsRolesIdEntity(username, roomId);

        UsersRoomsRolesEntity adminUser = new UsersRoomsRolesEntity(adminUserId,"ADMIN");
        usersRoomsRoles.add(adminUser);

        usersRoomsRolesRepository.saveAll(usersRoomsRoles);
        return "Room created and users, roles, rooms mapped";
    }


    public boolean isUserMember(String username, UUID roomId){
        return usersRoomsRolesRepository.findByIdUsernameAndIdRoomId(username,roomId).isPresent();
    }


}
