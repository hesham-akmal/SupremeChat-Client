package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

public class ChatActivity extends AppCompatActivity {

    //private RecyclerView mMessageRecycler;
    private RecyclerView messageRecycler;
    private MessageListAdapter messageAdapter;
    List<Message> messageList;

    EditText chatBox;
    Button sendBtn;
    static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = this;
        setTitle(getIntent().getStringExtra("name"));

        messageList = new ArrayList<>();

        messageList.add(new Message("test", User.mainUser));
        messageList.add(new Message("test1", User.mainUser));
        messageList.add(new Message("test2", User.mainUser));

        messageAdapter = new MessageListAdapter(this, messageList);
        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));



        sendBtn = (Button) findViewById(R.id.button_chatbox_send);

        chatBox = (EditText) findViewById(R.id.edittext_chatbox);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatBox.getText()!=null){
                    //new FriendConnection();
                    chatBox.setText("");
                    messageList.add(new Message(chatBox.getText().toString(), User.mainUser));
                    messageAdapter = new MessageListAdapter(c, messageList);
                    messageRecycler.setLayoutManager(new LinearLayoutManager(c));
                    messageRecycler.setAdapter(messageAdapter);

                }
            }
        });
    }


}
