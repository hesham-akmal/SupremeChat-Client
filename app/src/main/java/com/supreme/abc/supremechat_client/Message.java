package com.supreme.abc.supremechat_client;

public class Message {
    String message;
    User sender;


    public Message(String message, User sender){
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public User getSender(){return this.sender;}
}
