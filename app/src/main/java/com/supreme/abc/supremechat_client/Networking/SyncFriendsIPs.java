package com.supreme.abc.supremechat_client.Networking;

import android.os.AsyncTask;

import com.supreme.abc.supremechat_client.User;

import java.util.Hashtable;

import network_data.Command;

public class SyncFriendsIPs extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... s) {
        try {
            Network.instance.oos.writeObject(Command.sendFriends);
            Network.instance.oos.flush();
            Network.instance.oos.writeObject(User.mainUser.getFriendList());
            Network.instance.oos.flush();
            User.mainUser.setFriendList( (Hashtable<String, String>) Network.instance.ois.readObject()  );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}