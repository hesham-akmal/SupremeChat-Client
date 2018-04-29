package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.Networking.ListenToMessages;
import com.supreme.abc.supremechat_client.Networking.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import network_data.Command;
import network_data.Friend;
import network_data.MessagePacket;

public class ChatListActivity extends AppCompatActivity implements AsyncResponse{

    ImageView senderPic;
    public static String query;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SearchView searchView;
    static List<Friend> tempUser = new ArrayList<>();
    ListView chatListView;
    //This activity should contain Friends list fragment, and Chats fragment
    //mainUser MUST be logged in successfully and created to access this activity

    private ChatListAdapter chatListAdapter;


    ListenToMessages listen = new ListenToMessages();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ///SETUP///////////////////////////////////////
        //Sync Friends IPs from server
        AsyncTasks.SyncFriendsIPs();
        new UpdateFriendListGUI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //AsyncTasks.startListeningToClients();
        Network.instance.StartHeartbeatService();
        //AsyncTasks.ListenToMSGs();
        ////////////////////////////////////////////////
//        AsyncTasks.CheckConnection();
        listen.delegate=this;

        listen.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        editor = getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("ABC_key", MODE_PRIVATE);

        setTitle(getIntent().getStringExtra("username"));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
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
        );

        //getActionBar().setIcon(R.drawable.my_icon);

        chatListView = (ListView) findViewById(R.id.list);


        chatListAdapter = new ChatListAdapter(this, tempUser);

        chatListView.setAdapter(chatListAdapter);

        chatListView.setOnItemClickListener((adapterView, view, position, l) -> {


            Friend ttt = chatListAdapter.getItem(position);


            startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("Friend", ttt));

        });

    }

    @Override
    public void processFinish(MessagePacket output) {
        ChatActivity.messageList.add(output);
        ChatActivity.messageAdapter.notifyDataSetChanged();

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
        searchView.requestFocus();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ChatListActivity.query = query;

                if(User.mainUser.checkFriendExist(query)){
                    Toast.makeText(getApplicationContext(), "Friend already added!", Toast.LENGTH_LONG).show();
                    return false;
                }

                //Avoid calling "execute()" alone, as in lower versions of Android all
                // AsyncTasks were executed at single background thread. So new tasks might be waiting,
                // until other task working.
                new PerformSearch().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        tempUser.clear();//reset
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

    //Search and add friend if found
    public class PerformSearch extends AsyncTask<String, Void, Friend> {
        @Override
        protected Friend doInBackground(String... s) {

            Friend friend = null;

            synchronized(Network.instance.NetLock) {

                try {
                    Network.instance.oos.writeObject(Command.search);
                    Network.instance.oos.flush();

                    Network.instance.oos.writeObject(ChatListActivity.query);
                    Network.instance.oos.flush();

                    //Success. Username found and pass correct
                    if (Network.instance.ois.readObject() == Command.success) {
                        friend = (Friend) Network.instance.ois.readObject();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return friend;
        }

        @Override
        protected void onPostExecute(Friend friend) {

            if (friend != null) {
                //Friend f = new Friend(friend.getUsername(), friend.getStatus(), friend.getLastLogin(), friend.getIP());
                tempUser.add(friend);
                //chatListAdapter.add(f);
                User.mainUser.AddFriend(friend.getUsername(), friend);
                Toast.makeText(getApplicationContext(), "Friend Added!", Toast.LENGTH_LONG).show();
                chatListView.setAdapter(chatListAdapter);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "User doesn't exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class UpdateFriendListGUI extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... s) {
            for(Map.Entry<String, Friend> entry : Database.instance.LoadFriendList().entrySet()) {
                tempUser.add(entry.getValue());
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer i) {
            chatListView.setAdapter(chatListAdapter);
        }
    }



}
