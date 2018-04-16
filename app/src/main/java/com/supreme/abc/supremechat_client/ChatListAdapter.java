package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import network_data.Friend;

public class ChatListAdapter extends ArrayAdapter<Friend> {

    public ChatListAdapter(Context context, List<Friend> friends )
    {
        super(context, 0, friends);
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

        Friend currentUser = getItem(position);

        TextView senderTextView = (TextView) listItemView.findViewById(R.id.sender_name);
        senderTextView.setText(currentUser.getUsername());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.message_text);
        titleTextView.setText(currentUser.getUsername());

        return listItemView;
    }
}