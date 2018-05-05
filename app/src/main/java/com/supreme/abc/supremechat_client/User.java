package com.supreme.abc.supremechat_client;

import com.supreme.abc.supremechat_client.GroupChat.FriendGroup;

import java.util.Hashtable;

import network_data.Friend;

public class User {
    public static User mainUser = new User();

    private String username;
    private Hashtable<String, Friend> friendList;
    private Hashtable<String, FriendGroup> friendGroupList;

    //This should be called when the server authenticates the login info (user and pass are ok)
    public void Create(String username){
        //Syncing server user with "mainUser" object.
        this.username = username;
        friendList = Database.instance.LoadFriendList();
        friendGroupList = Database.instance.LoadGroupFriendList();
    }

    public void AddFriend(String username, Friend f){
        friendList.put(username,f);
        SaveFriendsDB();
    }
    public void AddFriendGroup(String username, FriendGroup group){
        friendGroupList.put(username,group);
        SaveGroupFriendsDB();
    }

    public Hashtable<String, Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(Hashtable<String, Friend> friendList) {
        this.friendList = friendList;
    }

    public String getUsername(){
        return this.username;
    }

    private void SaveFriendsDB(){
        Database.instance.SaveFriendList(friendList);
    }

    private void SaveGroupFriendsDB(){
        Database.instance.SaveGroupFriendList(friendGroupList);
    }

    public boolean checkFriendExist(String name){
        return getFriendList().containsKey(name);
    }

}


