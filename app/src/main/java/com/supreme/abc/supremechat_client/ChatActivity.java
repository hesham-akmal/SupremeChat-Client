package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.supreme.abc.supremechat_client.Networking.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;
import network_data.MessagePacket;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecycler;
    private static MessageListAdapter messageAdapter;
    public List<MessagePacket> messageList;
    static Friend friend;
    EditText chatBox;
    Button sendBtn;
    static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = this;
        friend = (Friend) getIntent().getSerializableExtra("Friend");
        setTitle(friend.getUsername());


//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//        Gson gson = new Gson();
//
//        String json = appSharedPrefs.getString(friend.getUsername(), "");
//
//        Type type = new TypeToken<List<MessagePacket>>() {
//        }.getType();
//        messageList = gson.fromJson(json, type);


        //main2
        //messageList = ChatListActivity.chatContainer.get(friend.getUsername()).messageList;
        //messageAdapter = ChatListActivity.chatContainer.get(friend.getUsername()).messageAdapter;
        //--

//        if (ChatListActivity.chatContainer.get(friend.getUsername()).messageList == null) {
//            messageList = new ArrayList<>();
//            messageAdapter = new MessageListAdapter(this, messageList);
//
//        } else {
//            messageList = ChatListActivity.chatContainer.get(friend.getUsername()).messageList;
//            messageAdapter = ChatListActivity.chatContainer.get(friend.getUsername()).messageAdapter;
//        }
        if (ChatListActivity.chatHistory.get(friend.getUsername()) == null) {
            messageList = new ArrayList<>();


        } else {
            messageList = ChatListActivity.chatHistory.get(friend.getUsername());

        }
        messageAdapter = new MessageListAdapter(this, messageList);

        if (messageList == null) {
            //messageList = ChatListActivity.chatLists.get(friend.getUsername());

        }
        if (messageAdapter == null) {
            messageAdapter = new MessageListAdapter(this, messageList);
        }


        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        //ChatListActivity.chatContainer.put(friend.getUsername(), new MessageContainer(messageRecycler, messageAdapter, messageList));

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
            }
        });
    }

    public static void NotifyDataSetChange(){
        messageAdapter.notifyDataSetChanged();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(messageList);
//        prefsEditor.putString(friend.getUsername(), json);
//        prefsEditor.apply();
//    }
}
