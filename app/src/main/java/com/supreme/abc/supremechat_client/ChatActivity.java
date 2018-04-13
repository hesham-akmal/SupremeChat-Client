package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;

public class ChatActivity extends StatusHandler {

    ImageView senderPic;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //This activity should contain Friends list fragment, and Chats fragment
    //mainUser MUST be logged in successfully and created to access this activity

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle(getIntent().getStringExtra("name"));

    }


}
