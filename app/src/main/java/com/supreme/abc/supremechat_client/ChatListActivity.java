package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network_data.Command;
import network_data.Friend;

public class ChatListActivity extends StatusHandler {

    ImageView senderPic;
    public static String query;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SearchView searchView;

    //This activity should contain Friends list fragment, and Chats fragment
    //mainUser MUST be logged in successfully and created to access this activity

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //Must write in every activity
        Network.instance.SetAlertDialogContext(ChatListActivity.this);

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

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ChatListActivity.query = query;
                new PerformSearch().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //getActionBar().setIcon(R.drawable.my_icon);

        ListView chatListView = (ListView) findViewById(R.id.list);

        //temp list of users
        List<Friend> tempUser = new ArrayList<>();
        Friend a = new Friend("user1", Friend.Status.Online, "6pm", "19.00.0");
        Friend b = new Friend("user2", Friend.Status.Online, "8pm", "19.00.0");
        Friend c = new Friend("user3", Friend.Status.Online, "7pm", "19.00.0");


        tempUser.add(a);
        tempUser.add(b);
        tempUser.add(c);
        chatAdapter = new ChatAdapter(this, tempUser);

        chatListView.setAdapter(chatAdapter);
    }

    private void LogOut() {
        editor.putBoolean("keep", false);
        editor.putString("username", null);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void GoToChat(View view) {
        LinearLayout v = (LinearLayout) view;
        TextView cc = (TextView) v.getChildAt(0);
        System.out.println(cc.getText());

        startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("name", cc.getText()));
    }


    public class PerformSearch extends AsyncTask <String, Void, Void> {
        @Override
        protected Void doInBackground(String ... s) {
            try {

                //TODO Send friend name to server and make sure they exist
                Network.instance.oos.writeObject(Command.search);
                Network.instance.oos.flush();

                Network.instance.oos.writeObject(ChatListActivity.query);
                Network.instance.oos.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

    }

}
