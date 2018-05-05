package com.supreme.abc.supremechat_client.GroupChat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supreme.abc.supremechat_client.Database;
import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.MyApplication;
import com.supreme.abc.supremechat_client.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import network_data.Friend;

public class GroupListFrag extends Fragment {

    //Recycler
    private static RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static List<FriendGroup> allFriendGroups = new ArrayList<>();
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

        MainActivity.ReloadGroupChatHistory();

        View rootView = inflater.inflate(R.layout.group_list_frag, container, false);

        recyclerView = rootView.findViewById(R.id.GroupsRecycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new GroupListAdapter(getContext(), allFriendGroups);
        recyclerView.setAdapter(mAdapter);
        new UpdateGroupFriendListGUI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return rootView;
    }

    public static void RefreshRecyclerView(FriendGroup fg) {
        GroupListFrag.allFriendGroups.add(fg);
        mAdapter.notifyDataSetChanged();
        Log.v("XXX", "333333333333333333333333333333333333333333333333333333333333333333333333"); //doesnt reach here for some reason
    }

    public static List<FriendGroup> getAllFriendGroups() {
        return allFriendGroups;
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
            GroupListFrag.mAdapter.notifyDataSetChanged();
        }
    }
}
