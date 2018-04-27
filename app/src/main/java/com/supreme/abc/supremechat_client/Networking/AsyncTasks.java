package com.supreme.abc.supremechat_client.Networking;

import android.os.AsyncTask;

import com.supreme.abc.supremechat_client.User;

import java.util.Hashtable;

import network_data.Command;
import network_data.Friend;

public class AsyncTasks {

    public static void SyncFriendsIPs(){
        new SyncFriendsIPs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

class SyncFriendsIPs extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... s) {
        try {
            Network.instance.oos.writeObject(Command.sendFriends);
            Network.instance.oos.flush();
            Network.instance.oos.writeObject(User.mainUser.getFriendList());
            Network.instance.oos.flush();
            User.mainUser.setFriendList( (Hashtable<String, Friend>) Network.instance.ois.readObject()  );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}