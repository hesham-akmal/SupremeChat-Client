package com.supreme.abc.supremechat_client.GroupChat;

import java.io.Serializable;
import java.util.ArrayList;

import network_data.Friend;

public class FriendGroup implements Serializable {

    public String title;
    private String groupName="";

    private ArrayList<Friend> allFriends;

    public FriendGroup(ArrayList<Friend> allFriends) {
        this.allFriends = allFriends;
        title = "New Group";
        for (Friend f : allFriends)
            groupName += f.getUsername() + " - ";
    }

    public void AddFriend(Friend friend) {
        this.allFriends.add(friend);
    }

    public ArrayList<Friend> getAllFriends() {
        return this.allFriends;
    }

    public String getFriendGroupName() {
        return groupName;
    }

    public void ClearFriendGroup() {
        this.allFriends.clear();
    }
}
