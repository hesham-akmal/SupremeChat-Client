package com.supreme.abc.supremechat_client.GroupChat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.MessageListAdapter;
import com.supreme.abc.supremechat_client.Networking.AsyncTasks;
import com.supreme.abc.supremechat_client.R;
import com.supreme.abc.supremechat_client.User;

import java.util.ArrayList;
import java.util.List;

import network_data.Friend;
import network_data.MessagePacket;

public class GroupChatActivity extends AppCompatActivity {

    private static RecyclerView messageRecycler;
    private static MessageListAdapter messageAdapter;
    public  static List<MessagePacket> messageList;
    public List<String> recipients = new ArrayList<>();
    public String title;
    public static FriendGroup friendGroup;
    static Friend friend;
    private EditText chatBox;
    private Button sendBtn;
    private static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = this;

        friendGroup = (FriendGroup) getIntent().getSerializableExtra("FriendGroup");
        title = friendGroup.getFriendGroupName();

        for (Friend f : friendGroup.getAllFriends()) {
            recipients.add(f.getUsername());
        }

        setTitle(title);
        if (MainActivity.groupChatHistory.get(title) == null || MainActivity.groupChatHistory.size() == 0) {
            MainActivity.groupChatHistory.put(friendGroup.getFriendGroupName(), new ArrayList<>());
            messageList = MainActivity.groupChatHistory.get(friendGroup.getFriendGroupName());
        } else {
            messageList = MainActivity.groupChatHistory.get(title);
        }
        messageAdapter = new MessageListAdapter(this, messageList);


        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        //MainActivity.chatContainer.put(friend.getUsername(), new MessageContainer(messageRecycler, messageAdapter, messageList));

        sendBtn = (Button) findViewById(R.id.button_chatbox_send);

        chatBox = (EditText) findViewById(R.id.edittext_chatbox);

        sendBtn.setOnClickListener(v -> {
            if (chatBox.getText() != null || !chatBox.getText().equals("")) {
                String text = chatBox.getText().toString();
                AsyncTasks.SendGroupMSGtoClient(recipients, text);
                messageList.add(new MessagePacket(User.mainUser.getUsername(), recipients, chatBox.getText().toString(), true));
                //MainActivity.groupChatHistory.get(friendGroup.getFriendGroupName()).add(new MessagePacket(User.mainUser.getUsername(), recipients, chatBox.getText().toString(), true));
                MainActivity.SaveGroupChatHistory();
                messageAdapter.notifyDataSetChanged();
                messageRecycler.smoothScrollToPosition(messageAdapter.getItemCount());
                NotifyDataSetChange();
                chatBox.setText("");
            }
        });
    }

    public static void NotifyDataSetChange() {
        if (messageAdapter == null) {
            if (friendGroup != null) {

                messageAdapter = new MessageListAdapter(c, MainActivity.groupChatHistory.get(friendGroup.getFriendGroupName()));
            }

            System.out.println("messageAdapter == null");
            return;
        }

        messageAdapter.notifyDataSetChanged();
        messageRecycler.smoothScrollToPosition(messageAdapter.getItemCount());

    }
}
