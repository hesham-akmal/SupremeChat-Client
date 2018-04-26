package com.supreme.abc.supremechat_client;

import java.util.Hashtable;

public class User {
    public static User mainUser;

    private String username;
    private String IP;
    private static Hashtable<String, String> friendList;

    //This should be called when the server authenticates the login info (user and pass are ok)
    public static void createMainUserObj(String username, String IP){
        //Syncing server user with "mainUser" object.
        mainUser = new User(username, IP);
        friendList = new Hashtable<>();
    }

    private User(String username, String IP) {
        this.username = username;
        this.IP = IP;
    }

    public void addFriend(String username, String IP){
        friendList.put(username,IP);
    }

    private void syncServer() {
        //send IP for "username" to server
    }
}


