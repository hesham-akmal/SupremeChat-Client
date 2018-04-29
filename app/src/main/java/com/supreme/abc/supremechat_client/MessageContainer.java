package com.supreme.abc.supremechat_client;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import network_data.MessagePacket;


public class MessageContainer implements Serializable {
    public MessageListAdapter messageAdapter;
    public List<MessagePacket> messageList;

    public MessageContainer() {
        messageList = new ArrayList<>();
        this.messageAdapter = new MessageListAdapter(ChatActivity.c, messageList);
    }

}
