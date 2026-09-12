package com.example.chitchat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadTestMessageRequest {
    @JsonProperty("client-name")
    private String clientName;
    private String msg;

    public LoadTestMessageRequest() {
    }

    public LoadTestMessageRequest(String clientName, String msg) {
        this.clientName = clientName;
        this.msg = msg;
    }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
}
