package com.supreme.abc.supremechat_client;

import java.util.Hashtable;

public class User {
    enum Status {
        Offline,
        Idle,
        Online
    }
    private String username;
    private boolean isAdmin;
    private Status status;
    private String IP;
    private Hashtable<String, String> friendList;

    public static User mainUser;
    //This should be called when the server authenticates the login info (user and pass are ok)
    public static void createMainUserObj(String username, boolean admin, String IP, Hashtable<String, String> friendList){
        //Syncing server user with "mainUser" object.
        mainUser = new User(username, admin, IP, friendList);
    }

    private User(String username, boolean isAdmin, String IP, Hashtable<String, String> friendList ) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.status = Status.Online;
        this.IP = IP;
        this.friendList = friendList;
    }

    private void updateStatusAndSync(Status status) {
        this.status = status;
        syncServer();
    }

    public void setIdleStatus() {
        updateStatusAndSync(Status.Idle);
    }

    public void setOfflineStatus() {
        updateStatusAndSync(Status.Offline);
    }

    public void addFriend(String username, String IP){
        friendList.put(username,IP);
        syncServer();
    }

    private void syncServer() {
        //send status,IP,friendlist for "username" to server

    }

    //Heart beat //For server to set Status and LastLogin for all clients.
    //Should be called by server every 5 seconds, if client disconnected abruptly then
    //server should automatically update Status as offline
    public boolean isAlive(){
        return true;
    }

}


