package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network_data.Command;
import network_data.Friend;

public class ChatListActivity extends AppCompatActivity {

    ImageView senderPic;
    public static String query;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SearchView searchView;
    List<Friend> tempUser;
    ListView chatListView;
    //This activity should contain Friends list fragment, and Chats fragment
    //mainUser MUST be logged in successfully and created to access this activity

    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //Must write in every activity
        //Network.instance.SetAlertDialogContext(ChatListActivity.this);
        Network.instance.StartHeartbeatService();

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

        chatListView = (ListView) findViewById(R.id.list);

        //temp list of users
        tempUser = new ArrayList<>();
        Friend a = new Friend("user1", Friend.Status.Online, "6pm", "19.00.0");
        Friend b = new Friend("user2", Friend.Status.Online, "8pm", "19.00.0");
        Friend c = new Friend("user3", Friend.Status.Online, "7pm", "19.00.0");


        tempUser.add(a);
        tempUser.add(b);
        tempUser.add(c);
        chatListAdapter = new ChatListAdapter(this, tempUser);

        chatListView.setAdapter(chatListAdapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Friend ttt = chatListAdapter.getItem(position);


                startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("name", ttt.getUsername()));

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

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

        return true;

    }


    private void LogOut() {
        editor.putBoolean("keep", false);
        editor.putString("username", null);
        editor.apply();
        Network.instance.StopHeartbeatService();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

//    public void GoToChat(View view) {
//        LinearLayout v = (LinearLayout) view;
//        TextView cc = (TextView) v.getChildAt(0);
//        System.out.println(cc.getText());
//
//        startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("name", cc.getText()));
//    }


    public class PerformSearch extends AsyncTask<String, Void, Friend> {
        @Override
        protected Friend doInBackground(String... s) {

            Friend friend = null;
            try {
                Network.instance.StopHeartbeatService();

                Network.instance.oos.writeObject(Command.search);
                Network.instance.oos.flush();

                if( Network.instance.ois.readObject() != Command.success )
                    return null;

                Network.instance.oos.writeObject(ChatListActivity.query);
                Network.instance.oos.flush();

                //Success. Username found and pass correct
                if ( Network.instance.ois.readObject() == Command.success) {
                    friend = (Friend) Network.instance.ois.readObject();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return friend;
        }

        @Override
        protected void onPostExecute(Friend friend) {

            if (friend != null) {
                chatListAdapter.add(new Friend(friend.getUsername(), friend.getStatus(), friend.getLastLogin(), friend.getIP()));
                User.mainUser.addFriend(friend.getUsername(), friend.getIP());
                Toast.makeText(getApplicationContext(), "Friend Added!", Toast.LENGTH_LONG).show();
                chatListView.setAdapter(chatListAdapter);
            }

            //Network.instance.StartHeartbeatService();

        }
    }
}
