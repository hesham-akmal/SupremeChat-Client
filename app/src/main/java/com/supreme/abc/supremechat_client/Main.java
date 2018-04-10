package com.supreme.abc.supremechat_client;

import android.os.Bundle;

public class Main extends StatusHandler {

    //This activity should contain Friends list fragment, and Chats fragment
    //User MUST be logged in to access this activity

    public static User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Syncing server user (Which is received from last activity) with "loggedUser" object.
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        boolean admin = getIntent().getBooleanExtra("admin",false);
        loggedUser = new User(username,password,admin);
        //loggedUser must be correctly initialized at this point

        //start LastLogin Timer
        LastLoginTimerHandler.postDelayed(LastLoginRunnable, 0);
    }
}
