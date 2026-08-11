package com.example.chitchat.repository;

import com.example.chitchat.entity.UsersRoomsRolesEntity;
import com.example.chitchat.entity.UsersRoomsRolesIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRoomsRolesRepository extends JpaRepository<UsersRoomsRolesEntity, UsersRoomsRolesIdEntity> {
    Optional<UsersRoomsRolesEntity> findByIdUsernameAndIdRoomId(String username, UUID roomId);
}
