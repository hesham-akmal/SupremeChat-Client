package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.Networking.ListenToMessages;
import com.supreme.abc.supremechat_client.Networking.Network;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import network_data.Command;
import network_data.Friend;
import network_data.MessagePacket;

public class ChatActivity extends AppCompatActivity{

    private RecyclerView messageRecycler;
    public static MessageListAdapter messageAdapter;
    public static List<MessagePacket> messageList;
    static Friend friend;
    EditText chatBox;
    Button sendBtn;
    static Context c;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = this;
        friend = (Friend) getIntent().getSerializableExtra("Friend");
        setTitle(friend.getUsername());

        prefs = getSharedPreferences("App_settings", MODE_PRIVATE);

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();

        String json = appSharedPrefs.getString(friend.getUsername(), "");

        Type type = new TypeToken<List<MessagePacket>>() {
        }.getType();
        messageList = gson.fromJson(json, type);



        if (messageList == null) {
            messageList = new ArrayList<>();

        }
        if (messageAdapter == null) {
            messageAdapter = new MessageListAdapter(this, messageList);
        }

        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        sendBtn = (Button) findViewById(R.id.button_chatbox_send);

        chatBox = (EditText) findViewById(R.id.edittext_chatbox);

        sendBtn.setOnClickListener(v -> {
            if (chatBox.getText() != null) {
                String text = chatBox.getText().toString();
                Network.instance.StopHeartbeatService();
                AsyncTasks.SendMSGtoClient(friend.getUsername(),text);
                messageList.add(new MessagePacket(User.mainUser.getUsername(), friend.getUsername(), chatBox.getText().toString()));
                messageAdapter.notifyDataSetChanged();
                Network.instance.StartHeartbeatService();
                chatBox.setText("");
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messageList);
        prefsEditor.putString(friend.getUsername(), json);
        prefsEditor.apply();
    }
}
