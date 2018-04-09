package com.supreme.abc.supremechat_client;

import java.util.Calendar;

public class User {
    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.status = Status.Online;
        setLastLogin(Calendar.getInstance().getTime().toString());
        syncServer();
    }

    enum Status {
        Offline,
        Idle,
        Online;
    };

    private String username;
    private String password;
    private boolean admin;
    private String lastLogin;
    private Status status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatusAndSync(Status status) {
        this.status = status;
        syncServer();
    }

    public void updateLastLoginAndSync(){
        setLastLogin(Calendar.getInstance().getTime().toString());
        syncServer();
    }

    private void syncServer() {
        //send Status and LastLogin to server

    }

    //Heart beat
    //Should be called by server every 5 seconds, if client disconnected abruptly then
    //server should automatically update Status as offline
    public boolean isAlive(){
        return true;
    }

}
