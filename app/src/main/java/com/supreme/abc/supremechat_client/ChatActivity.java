package com.supreme.abc.supremechat_client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView senderPic;
    private BottomNavigationView bottomNavigationView;
    //This activity should contain Friends list fragment, and Chats fragment
    //User MUST be logged in to access this activity
    public static User loggedUser;


    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
                                // TODO
                                return true;
                        }
                        return false;
                    }
                }
        );


        //getActionBar().setIcon(R.drawable.my_icon);

        //Syncing server user (Which is received from last activity) with "loggedUser" object.
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        boolean admin = getIntent().getBooleanExtra("admin", false);
        loggedUser = new

                User(username, password, admin);
        //loggedUser must be correctly initialized at this point


        ListView chatListView = (ListView) findViewById(R.id.list);

        //temp list of users
        List<User> tempUser = new ArrayList<>();
        User a = new User("user1", "1", false);
        User b = new User("user2", "2", false);
        User c = new User("user3", "3", false);

        tempUser.add(a);
        tempUser.add(b);
        tempUser.add(c);
        chatAdapter = new

                ChatAdapter(this, tempUser);

        chatListView.setAdapter(chatAdapter);


        //start LastLogin Timer
        //LastLoginTimerHandler.postDelayed(LastLoginRunnable, 0);
    }
}
