package com.supreme.abc.supremechat_client.GroupChat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.MyApplication;
import com.supreme.abc.supremechat_client.R;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;

public class GroupListFrag extends Fragment {

    //Recycler
    private static RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static List<FriendGroup> allFriendGroups = new ArrayList<>();
    ////////////

    //List<MessagePacket> messageList = new ArrayList<>();
    //public static GroupListAdapter groupChatListAdapter;
    //public static Hashtable<String, MessageContainer> chatContainer;
    //public static Hashtable <String, List<MessagePacket>> chatLists;

    public static GroupListFrag newInstance() {
        GroupListFrag fragment = new GroupListFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.group_list_frag, container, false);

        recyclerView = rootView.findViewById(R.id.GroupsRecycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new GroupListAdapter(getContext(), allFriendGroups);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public static void addFriendGroup(FriendGroup fg){
        Log.v("XXX","NEW FG ADDED: ");

        for(Friend f : fg.getAllFriends())
            Log.v("XXX",f.getUsername());

        allFriendGroups.add(fg);
        mAdapter = new GroupListAdapter(MainActivity.context, allFriendGroups);
        recyclerView.setAdapter(mAdapter);
        Log.v("XXX","3"); //doesnt reach here for some reason
    }

    public static List<FriendGroup> getAllFriendGroups(){
        return allFriendGroups;
    }
}
