package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;

public class ChatActivity extends StatusHandler {

    ImageView senderPic;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs ;
    private SharedPreferences.Editor editor;

    //This activity should contain Friends list fragment, and Chats fragment
    //mainUser MUST be logged in successfully and created to access this activity

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editor = getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("ABC_key", MODE_PRIVATE);

        setTitle(getIntent().getStringExtra("username"));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_bar_item_calls:
                                // TODO
                                return true;
                            case R.id.bottom_bar_item_recents:
                                // TODO
                                return true;
                            case R.id.bottom_bar_item_trips:
                                LogOut();
                                return true;
                        }
                        return false;
                    }
                }
        );


        //getActionBar().setIcon(R.drawable.my_icon);

        ListView chatListView = (ListView) findViewById(R.id.list);

        //temp list of users
        List<Friend> tempUser = new ArrayList<>();
        Friend a = new Friend("user1", Friend.Status.Online,"6pm","19.00.0");
        Friend b = new Friend("user2", Friend.Status.Online,"8pm","19.00.0");
        Friend c = new Friend("user3", Friend.Status.Online,"7pm","19.00.0");

        tempUser.add(a);
        tempUser.add(b);
        tempUser.add(c);
        chatAdapter = new ChatAdapter(this, tempUser);

        chatListView.setAdapter(chatAdapter);

        //start LastLogin Timer
        //LastLoginTimerHandler.postDelayed(LastLoginRunnable, 0);
    }

    private void LogOut(){
        editor.putBoolean("keep", false);
        editor.putString("username", null);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
