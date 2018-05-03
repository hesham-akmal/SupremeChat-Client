package com.supreme.abc.supremechat_client.GroupChat;

import java.io.Serializable;
import java.util.ArrayList;
import network_data.Friend;

public class FriendGroup implements Serializable {

    public String title;

    private ArrayList<Friend> allFriends;

    public FriendGroup(ArrayList<Friend> allFriends){
        this.allFriends = allFriends;
        title = "NEw Group";
    }

    public void AddFriend(Friend friend){
        this.allFriends.add(friend);
    }

    public ArrayList<Friend> getAllFriends(){
        return this.allFriends;
    }

    public void ClearFriendGroup(){this.allFriends.clear();}
}
