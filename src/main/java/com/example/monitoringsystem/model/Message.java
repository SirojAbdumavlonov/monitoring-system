package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class Message {
    private String typeOfMessage;
    private String content;

    public Message(String typeOfMessage, String content){
        this.typeOfMessage = typeOfMessage;
        this.content = content;
    }
    public Message(){}
}
