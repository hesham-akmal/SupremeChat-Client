package com.supreme.abc.supremechat_client;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends StatusHandler {

    ImageView senderPic;
    //This activity should contain Friends list fragment, and Chats fragment
    //User MUST be logged in to access this activity
    public static User loggedUser;


    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle(getIntent().getStringExtra("username"));
        //getActionBar().setIcon(R.drawable.my_icon);

        //Syncing server user (Which is received from last activity) with "loggedUser" object.
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        boolean admin = getIntent().getBooleanExtra("admin",false);
        loggedUser = new User(username,password,admin);
        //loggedUser must be correctly initialized at this point


        ListView chatListView = (ListView) findViewById(R.id.list);

        //temp list of users
        List<User> tempUser = new ArrayList<>();
        User a = new User("1", "1", false);
        User b = new User("2", "2", false);
        User c = new User("3", "3", false);

        tempUser.add(a);
        tempUser.add(b);
        tempUser.add(c);
        chatAdapter = new ChatAdapter(this, tempUser);

        chatListView.setAdapter(chatAdapter);


        //start LastLogin Timer
        LastLoginTimerHandler.postDelayed(LastLoginRunnable, 0);
    }
}
