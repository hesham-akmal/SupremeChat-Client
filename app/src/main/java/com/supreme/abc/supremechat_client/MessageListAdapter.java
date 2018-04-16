package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import network_data.Friend;

public class MessageListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Message> messageList;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public MessageListAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) messageList.get(position);

        if (message.getSender().equals(User.mainUser)) {
            System.out.println("getItemViewType called");
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            System.out.println("getItemViewType called");
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            System.out.println("Viewholder called");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            System.out.println("Viewholder called");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_recieved, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                System.out.println("onbind called");
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                System.out.println("Viewholder called");
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;
        //ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            //timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;
        //ImageView profileImage;

        SentMessageHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            //timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

        }
    }
}

