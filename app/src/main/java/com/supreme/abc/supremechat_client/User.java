package com.supreme.abc.supremechat_client;

import java.util.Hashtable;

import network_data.Friend;

public class User {
    public static User mainUser = new User();

    private String username;
    private Hashtable<String, Friend> friendList;

    //This should be called when the server authenticates the login info (user and pass are ok)
    public void Create(String username){
        //Syncing server user with "mainUser" object.
        this.username = username;
        friendList = Database.instance.LoadFriendList();
    }

    public void AddFriend(String username, Friend f){
        friendList.put(username,f);
        SaveFriendsDB();
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

    public boolean checkFriendExist(String name){
        return getFriendList().containsKey(name);
    }

}


