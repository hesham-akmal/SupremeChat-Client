package com.supreme.abc.supremechat_client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import network_data.MessagePacket;

public class MessageContainer implements Serializable {
    public List<MessagePacket> messageList;
    public MessageContainer() {
        messageList = new ArrayList<>();
    }

}
