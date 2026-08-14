package com.example.chitchat.dto;

import java.util.ArrayList;
import java.util.Set;

public class CreateRoomRequest {

    private String roomname;
    private Set<String> participants;
    private int maximumCapacity;
    private java.util.Map<String, String> userKeys;

    public java.util.Map<String, String> getUserKeys() {
        return userKeys;
    }

    public void setUserKeys(java.util.Map<String, String> userKeys) {
        this.userKeys = userKeys;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
}
