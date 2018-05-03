package com.supreme.abc.supremechat_client.FriendChat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.MessageListAdapter;
import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.R;
import com.supreme.abc.supremechat_client.User;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;
import network_data.MessagePacket;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecycler;
    private static MessageListAdapter messageAdapter;
    public List<MessagePacket> messageList;
    static Friend friend;
    private EditText chatBox;
    private Button sendBtn;
    private static Context c;
    //private Gson gson;
    //private SharedPreferences appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = this;
        friend = (Friend) getIntent().getSerializableExtra("Friend");
        setTitle(friend.getUsername());


        /*appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        gson = new Gson();*/

        //main2
        //messageList = MainActivity.chatContainer.get(friend.getUsername()).messageList;
        //messageAdapter = MainActivity.chatContainer.get(friend.getUsername()).messageAdapter;
        //--

//        if (MainActivity.chatContainer.get(friend.getUsername()).messageList == null) {
//            messageList = new ArrayList<>();
//            messageAdapter = new MessageListAdapter(this, messageList);
//
//        } else {
//            messageList = MainActivity.chatContainer.get(friend.getUsername()).messageList;
//            messageAdapter = MainActivity.chatContainer.get(friend.getUsername()).messageAdapter;
//        }
        if (MainActivity.chatHistory.get(friend.getUsername()) == null) {
            messageList = new ArrayList<>();


        } else {
            messageList = MainActivity.chatHistory.get(friend.getUsername());

        }
        messageAdapter = new MessageListAdapter(this, messageList);

        if (messageList == null) {
            //messageList = MainActivity.chatLists.get(friend.getUsername());

        }
        if (messageAdapter == null) {
            messageAdapter = new MessageListAdapter(this, messageList);
        }


        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        //MainActivity.chatContainer.put(friend.getUsername(), new MessageContainer(messageRecycler, messageAdapter, messageList));

        sendBtn = (Button) findViewById(R.id.button_chatbox_send);

        chatBox = (EditText) findViewById(R.id.edittext_chatbox);

        sendBtn.setOnClickListener(v -> {
            if (chatBox.getText() != null || !chatBox.getText().equals("")) {
                String text = chatBox.getText().toString();
                AsyncTasks.SendMSGtoClient(friend.getUsername(), text);
                messageList.add(new MessagePacket(User.mainUser.getUsername(), friend.getUsername(), chatBox.getText().toString()));
                messageAdapter.notifyDataSetChanged();
                messageRecycler.smoothScrollToPosition(messageAdapter.getItemCount());
                chatBox.setText("");

                saveChat();
            }
        });
    }

    public static void NotifyDataSetChange(){
        messageAdapter.notifyDataSetChanged();
    }

    public void saveChat() {
        /*String json = gson.toJson(messageList);
        prefsEditor.putString(friend.getUsername(), json);
        prefsEditor.apply();*/

    }
}
