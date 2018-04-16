package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;

public class ChatActivity extends StatusHandler {

    //private RecyclerView mMessageRecycler;
    private ListView messageListView;
    private MessageListAdapter messageAdapter;
    List <Message> messageList;

    EditText chatBox;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle(getIntent().getStringExtra("name"));

        messageList = new ArrayList<>();

        messageList.add(new Message("test", User.mainUser));

//        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
//        mMessageAdapter = new MessageListAdapter(this, messageList);
//        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mMessageRecycler.setAdapter(mMessageAdapter);
        messageListView = (ListView) findViewById(R.id.reyclerview_message_list);
        sendBtn = (Button)findViewById(R.id.button_chatbox_send);

        chatBox = (EditText) findViewById(R.id.edittext_chatbox);


        messageAdapter = new MessageListAdapter(this, messageList);

        messageListView.setAdapter(messageAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatBox.getText()!=null){
                    messageList.add(new Message(chatBox.getText().toString(), User.mainUser));
                    messageAdapter.add(new Message(chatBox.getText().toString(), User.mainUser));
                    messageListView.setAdapter(messageAdapter);
                }
            }
        });
    }


}
