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

public class MessageListAdapter extends ArrayAdapter<Message> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public MessageListAdapter(Context context, List<Message> messageList) {
        super(context, 0, messageList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_item, parent, false);
        }

        Message message = getItem(position);

        // If the current user is the sender of the message
        if (message.getSender().equals(User.mainUser)) {

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_sent, parent, false);
            TextView text_message_body = (TextView) listItemView.findViewById(R.id.reyclerview_message_list);
            text_message_body.setText(message.getMessage());
            return listItemView;
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_recieved, parent, false);
            TextView text_message_body = (TextView) listItemView.findViewById(R.id.reyclerview_message_list);
            text_message_body.setText(message.getMessage());
            return listItemView;
        }
    }

//    // Inflates the appropriate layout according to the ViewType.
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//
//        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_message_sent, parent, false);
//            return new SentMessageHolder(view);
//        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_message_recieved, parent, false);
//            return new ReceivedMessageHolder(view);
//        }
//
//        return null;
//    }
//
//    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Message message = (Message) mMessageList.get(position);
//
//        switch (holder.getItemViewType()) {
//            case VIEW_TYPE_MESSAGE_SENT:
//                ((SentMessageHolder) holder).bind(message);
//                break;
//            case VIEW_TYPE_MESSAGE_RECEIVED:
//                ((ReceivedMessageHolder) holder).bind(message);
//        }
//    }
//
//    private class SentMessageHolder extends RecyclerView.ViewHolder {
//        TextView messageText, timeText;
//
//        SentMessageHolder(View itemView) {
//            super(itemView);
//
//            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
//        }
//
//        void bind(Message message) {
//            messageText.setText(message.getMessage());
//
//            // Format the stored timestamp into a readable String using method.
//            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
//        }
//    }
//
//    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
//        TextView messageText, timeText, nameText;
//        ImageView profileImage;
//
//        ReceivedMessageHolder(View itemView) {
//            super(itemView);
//
//            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
//            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
//            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
//            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
//        }
//
//        void bind(Message message) {
//            messageText.setText(message.getMessage());
//
//            // Format the stored timestamp into a readable String using method.
//            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
//
//            //nameText.setText(message.getSender().getNickname());
//
//            // Insert the profile image from the URL into the ImageView.
//            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
//        }
//    }
}

