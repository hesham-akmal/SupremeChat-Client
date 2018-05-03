package com.supreme.abc.supremechat_client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import network_data.Friend;
import network_data.MessagePacket;

public class ItemTwoFragment extends Fragment {

    ListView chatListView;
    ImageView senderPic;
    List<MessagePacket> messageList = new ArrayList<>();
    public static List<Friend> tempUser = new ArrayList<>();

    public static GroupChatListAdapter groupChatListAdapter;
    public static Hashtable<String, MessageContainer> chatContainer;
    //    public static Hashtable <String, List<MessagePacket>> chatLists;
    SharedPreferences sharedPrefs;

    public static List<FriendGroup> friendGroup = new ArrayList<>();

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_item_two, container, false);

        chatListView = (ListView) rootView.findViewById(R.id.groupchat_list);

        groupChatListAdapter = new GroupChatListAdapter(getContext(), friendGroup);

        chatListView.setAdapter(groupChatListAdapter);

        return rootView;
    }
}