package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supreme.abc.supremechat_client.FriendChat.FriendListFrag;
import com.supreme.abc.supremechat_client.GroupChat.FriendGroup;
import com.supreme.abc.supremechat_client.GroupChat.GroupListFrag;
import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.Networking.Network;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import network_data.Command;
import network_data.Friend;
import network_data.MessagePacket;

public class MainActivity extends AppCompatActivity {

    public static SearchView searchView;
    public static String query;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static Hashtable<String, List<MessagePacket>> chatHistory;
    public static Hashtable<String, List<MessagePacket>> groupChatHistory;
    public static ArrayList<String> allChosenFriendsGroup = new ArrayList<>();
    public static List<Friend> tempUser = new ArrayList<>();
    public static List<FriendGroup> friendGroups = new ArrayList<>();
    public static Context context;

    public static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        context = this;

        ReloadChatHistory();
        ReloadGroupChatHistory();
        ReloadFriendsGroupList();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (item -> {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.action_item1:
                            selectedFragment = FriendListFrag.newInstance();
                            break;
                        case R.id.action_item2:
                            selectedFragment = GroupListFrag.newInstance();
                            bottomNavigationView.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.g1));
                            break;
                        case R.id.action_item3:
                            selectedFragment = ItemThreeFragment.newInstance();
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                    return true;
                });
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, FriendListFrag.newInstance());
        transaction.commit();


        ///SETUP///////////////////////////////////////
        //Sync Friends IPs from server
        AsyncTasks.SyncFriendsIPs();
        new UpdateFriendListGUI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        Network.instance.StartHeartbeatService();

        AsyncTasks.ListenToMessages();

        editor = getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("ABC_key", MODE_PRIVATE);

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
        new UpdateGroupFriendListGUI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public static void ReloadFriendsGroupList() {
        //CHATHSITORY

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        String json = appSharedPrefs.getString("GroupFriendList", "");

        Type type = new TypeToken<List<FriendGroup>>() {
        }.getType();
        friendGroups = gson.fromJson(json, type);
        if (friendGroups  == null) {
            friendGroups  = new ArrayList<>();
        }
    }

    public static void ReloadChatHistory() {
        //CHATHSITORY

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        String json = appSharedPrefs.getString("chatHistory", "");

        Type type = new TypeToken<Hashtable<String, List<MessagePacket>>>() {
        }.getType();
        chatHistory = gson.fromJson(json, type);
        if (chatHistory == null) {
            chatHistory = new Hashtable<>();
        }
    }

    public static void ReloadGroupChatHistory(){
        //CHATHSITORY

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        String json = appSharedPrefs.getString("groupChatHistory", "");

        Type type = new TypeToken<Hashtable <String, List<MessagePacket>>>() {}.getType();
        groupChatHistory= gson.fromJson(json, type);
        if (groupChatHistory== null) {
            groupChatHistory= new Hashtable<>();
        }

    }

    public static void SaveChatHistory() {
        SharedPreferences settings = context.getSharedPreferences("chatHistory", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(chatHistory);
        prefsEditor.putString("chatHistory", json);
        prefsEditor.commit();

    }

    public static void SaveGroupChatHistory() {
        SharedPreferences settings = context.getSharedPreferences("groupChatHistory", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(groupChatHistory);
        prefsEditor.putString("groupChatHistory", json);
        prefsEditor.commit();

        ReceivedGroupINV();
    }

    public static void ReceivedGroupINV(){
        //FriendListAdapter.clearAll();

        //return normal group icon
        bottomNavigationView.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.g2));
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
                MainActivity.query = query;

                if (User.mainUser.checkFriendExist(query)) {
                    Toast.makeText(getApplicationContext(), "Friend already added!", Toast.LENGTH_LONG).show();
                    chatHistory.put(query, new ArrayList<>());
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    SaveChatHistory();
                    return false;
                }
                if (!query.equals("") || query != null) {
                    new PerformSearch().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


//    public void AddToGroup(View view) {
//        //GroupListFrag.friendGroup.add(FriendListFrag.friendGroup);
//        //GroupListFrag.groupChatListAdapter.notifyDataSetChanged();
//        //findViewById(R.id.add_to_group_button).setVisibility(View.VISIBLE);
//    }


    public class PerformSearch extends AsyncTask<String, Void, Friend> {
        @Override
        protected Friend doInBackground(String... s) {

            Friend friend = null;

            synchronized (Network.instance.NetLock) {

                try {
                    Network.instance.oos.writeObject(Command.search);
                    Network.instance.oos.flush();

                    Network.instance.oos.writeObject(MainActivity.query);
                    Network.instance.oos.flush();

                    //Success. Username found and pass correct
                    if (Network.instance.ois.readObject() == Command.success) {
                        friend = (Friend) Network.instance.ois.readObject();
                        chatHistory.put(query, new ArrayList<>());
                        SaveChatHistory();
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
                //tempUser.add(friend);
                //chatListAdapter.add(f);
                User.mainUser.AddFriend(friend.getUsername(), friend);
                Toast.makeText(getApplicationContext(), "Friend Added!", Toast.LENGTH_LONG).show();
                FriendListFrag.tempUser.add(friend);
                FriendListFrag.mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "User doesn't exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class UpdateFriendListGUI extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... s) {
            for (Map.Entry<String, Friend> entry : Database.instance.LoadFriendList().entrySet()) {
                tempUser.add(entry.getValue());
                FriendListFrag.tempUser.add(entry.getValue());
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer i) {
            FriendListFrag.mAdapter.notifyDataSetChanged();
        }
    }

    public static class UpdateGroupFriendListGUI extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... s) {
            for (Map.Entry<String, FriendGroup> entry : Database.instance.LoadGroupFriendList().entrySet()) {
                MainActivity.friendGroups.add(entry.getValue());
                GroupListFrag.allFriendGroups.add(entry.getValue());
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer i) {
            if(GroupListFrag.mAdapter!=null)
                GroupListFrag.mAdapter.notifyDataSetChanged();
        }
    }

}
