package com.supreme.abc.supremechat_client;

import java.util.Hashtable;

public class User {
    public static User mainUser = new User();

    private String username;
    private String IP;
    private Hashtable<String, String> friendList;

    //This should be called when the server authenticates the login info (user and pass are ok)
    public void Create(String username, String IP){
        //Syncing server user with "mainUser" object.
        this.username = username;
        this.IP = IP;
        //Check if there's a friend list in shared prefs first,Implement later
        friendList = new Hashtable<>();
    }

    public void AddFriend(String username, String IP){
        friendList.put(username,IP);
    }

    public Hashtable<String, String> getFriendList() {
        return friendList;
    }
    public void setFriendList(Hashtable<String, String> friendList) {
        this.friendList = friendList;
    }

    public String getUsername(){
        return this.username;
    }

    private void syncServer() {
        //send IP for "username" to server
    }
}


