package com.supreme.abc.supremechat_client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import network_data.Friend;

public class FriendGroup implements Serializable {
    ArrayList<Friend> friendGroup;
    public FriendGroup(){
        friendGroup = new ArrayList<>();
    }

    public void AddFriend(Friend friend){
        this.friendGroup.add(friend);
    }

    public ArrayList<Friend> GetFriendGroup(){
        return this.friendGroup;
    }

    public void ClearFriendGroup(){this.friendGroup.clear();}
}
