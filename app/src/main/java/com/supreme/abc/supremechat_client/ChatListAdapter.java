package com.supreme.abc.supremechat_client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import network_data.Friend;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context context;
    private List<Friend> personUtils;

    public ChatListAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        Friend fr = personUtils.get(position);

        holder.title.setText(fr.getUsername());
        holder.desc.setText(fr.getLastLogin());

        holder.fr = fr;
    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView desc;
        private ImageView chosenIcon;
        public Friend fr;
        private boolean chosen;

        public ViewHolder(View itemView) {
            super(itemView);

            chosen = false;

            title =  itemView.findViewById(R.id.sender_name);
            desc =  itemView.findViewById(R.id.message_text);
            chosenIcon = itemView.findViewById(R.id.chosenIcon);

            itemView.setOnClickListener(view -> {
                Friend fr = (Friend) view.getTag();
                context.startActivity(new Intent(MyApplication.getAppContext(), ChatActivity.class).putExtra("Friend", fr));
            });

            itemView.setOnLongClickListener(view -> {
                LongHoldHandler();
                return true;
            });
        }

        private void LongHoldHandler()
        {
            if(!chosen)
            {
                chosen = true;
                chosenIcon.setVisibility(View.VISIBLE);
                ChatListActivity.allChosenFriendsGroup.add(fr.getUsername());
            }
            else
            {
                chosen = false;
                chosenIcon.setVisibility(View.GONE);
                ChatListActivity.allChosenFriendsGroup.remove(fr.getUsername());
            }
        }
    }
}