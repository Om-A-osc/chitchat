package com.example.chitchat.entity;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class UsersRoomsRolesIdEntity {
    private String username;
    private UUID roomId;

    public UsersRoomsRolesIdEntity(){}

    public UsersRoomsRolesIdEntity( String username , UUID roomId ){
        this.username = username;
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if( !(o instanceof UsersRoomsRolesIdEntity) ) return false;
        UsersRoomsRolesIdEntity that = (UsersRoomsRolesIdEntity)o;
        return username.equals(that.getUsername()) && roomId.equals(that.getRoomId());
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, roomId);
    }

}
