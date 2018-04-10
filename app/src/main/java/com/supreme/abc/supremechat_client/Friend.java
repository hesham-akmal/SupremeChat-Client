package com.supreme.abc.supremechat_client;

public class Friend {
    enum Status {
        Offline,
        Idle,
        Online
    }

    private String username;
    private boolean admin;
    private Status status;
    private String lastLogin;
    private String IP;

    public Friend(String username, boolean admin, Status status, String lastLogin, String IP) {
        this.username = username;
        this.admin = admin;
        this.status = status;
        this.lastLogin = lastLogin;
        this.IP = IP;
    }

    public String getUsername() {
        return username;
    }

    public Status getStatus() {
        return status;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getIP() {
        return IP;
    }
}