package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.Networking.Network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import network_data.Friend;
import network_data.MessagePacket;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;
import static com.supreme.abc.supremechat_client.ChatActivity.friend;

public class ItemOneFragment extends Fragment {

    RecyclerView chatRecyclerView;
    ImageView senderPic;
    List<MessagePacket> messageList = new ArrayList<>();
    public static List<Friend> tempUser = new ArrayList<>();

    //public static ChatListAdapter chatListAdapter;
    //public static Hashtable<String, MessageContainer> chatContainer;
    public static FriendGroup friendGroup = new FriendGroup();
    SharedPreferences sharedPrefs;

    RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<String> allChosenFriendsGroup = new ArrayList<>();


    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_one, container, false);

//        if (chatContainer == null) {
//            chatContainer = new Hashtable<>();
//        }
//
//        chatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_list);
//
//
//        chatListAdapter = new ChatListAdapter(getContext(), tempUser);
//
//        chatRecyclerView.setAdapter(chatListAdapter);

        recyclerView = rootView.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatListAdapter(getContext(), tempUser);
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }


}

