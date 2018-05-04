package com.supreme.abc.supremechat_client.FriendChat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.supreme.abc.supremechat_client.GroupChat.GroupListFrag;
import com.supreme.abc.supremechat_client.ItemThreeFragment;
import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.R;
import com.supreme.abc.supremechat_client.User;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;
import network_data.MessagePacket;

public class FriendListFrag extends Fragment {

    RecyclerView chatRecyclerView;
    ImageView senderPic;
    List<MessagePacket> messageList = new ArrayList<>();
    public static List<Friend> tempUser = new ArrayList<>();
    public static FloatingActionButton new_group_fab;
    //private String m_Text = "";

    //public static FriendListAdapter chatListAdapter;
    //public static Hashtable<String, MessageContainer> chatContainer;
    //private SharedPreferences sharedPrefs;

    private RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static FriendListFrag newInstance() {
        FriendListFrag fragment = new FriendListFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_list_frag, container, false);

        //New Group fab init////////////////////////
        new_group_fab = rootView.findViewById(R.id.new_group_fab);
        new_group_fab.setOnClickListener(view -> CreateNewGroup1());

        ///Recycler init //New Group fab init////////////////////////
        recyclerView = rootView.findViewById(R.id.FriendsRecycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FriendListAdapter(getContext(), tempUser);
        recyclerView.setAdapter(mAdapter);
        ///////////////////////////////////////////////////////////////

        return rootView;
    }

    public void CreateNewGroup1(){
        //add myself to the group
        MainActivity.allChosenFriendsGroup.add(User.mainUser.getUsername());
        AsyncTasks.SendGroupInvServer(MainActivity.allChosenFriendsGroup);
        MainActivity.SaveGroupChatHistory();
        //////////////setting title
        /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Type Group Name");
        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                AsyncTasks.SendGroupInvServer(MainActivity.allChosenFriendsGroup);
            }
        });
        builder.show();*/
        /////////////
    }
}

