package com.supreme.abc.supremechat_client.GroupChat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supreme.abc.supremechat_client.FriendChat.ChatActivity;
import com.supreme.abc.supremechat_client.MyApplication;
import com.supreme.abc.supremechat_client.R;

import java.util.List;

import network_data.Friend;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private Context context;
    String allFriendNamesInsideGroup = "";
    private List<FriendGroup> allFriendGroups;

    public GroupListAdapter(Context context, List personUtils) {
        this.context = context;
        this.allFriendGroups = personUtils;
    }

    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        GroupListAdapter.ViewHolder viewHolder =  new GroupListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupListAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(allFriendGroups.get(position));

        FriendGroup fg = allFriendGroups.get(position);

        holder.title.setText(fg.title);

        allFriendNamesInsideGroup = fg.getFriendGroupName();

//        for(Friend f : fg.getAllFriends())
//            allFriendNamesInsideGroup += f.getUsername() + " - ";

        holder.desc.setText(allFriendNamesInsideGroup);
    }

    @Override
    public int getItemCount() {
        return allFriendGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);

            title =  itemView.findViewById(R.id.sender_name);
            desc =  itemView.findViewById(R.id.message_text);

            itemView.setOnClickListener(view -> {
                FriendGroup  friendGroup = (FriendGroup) view.getTag();
                Intent i = new Intent(MyApplication.getAppContext(), GroupChatActivity.class);
                i.putExtra("FriendGroup", friendGroup);
                i.putExtra("title", allFriendNamesInsideGroup);
                context.startActivity(i);
            });



            //itemView.setOnLongClickListener(view -> {
            //   return true;
            //});
        }
    }
}

