package com.supreme.abc.supremechat_client.Networking;

import android.os.AsyncTask;
import android.util.Log;

//import com.supreme.abc.supremechat_client.MainActivity;
//import com.supreme.abc.supremechat_client.MessageContainer;
import com.supreme.abc.supremechat_client.FriendChat.ChatActivity;
import com.supreme.abc.supremechat_client.GroupChat.FriendGroup;
import com.supreme.abc.supremechat_client.GroupChat.GroupListFrag;
import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.User;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;

import network_data.Command;
import network_data.Friend;
import network_data.MessagePacket;

public class AsyncTasks {

    public static void SyncFriendsIPs(){
        new SyncFriendsIPs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /*public static void startListeningToClients(){
        //IN CASE WE'LL USE SAME NETWORK //new ListenToClients().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/

    public static void SendMSGtoClient(String friendName, String text){
        //IN CASE WE'LL USE SAME NETWORK  //new FriendClient(IP,text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SendMSG(friendName,text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

//    public static void CheckConnection(){
//        new checkConnection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }

    public static void ListenToMessages(){
        new ListenToMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void SendGroupInvServer(ArrayList<String> allFriendsNames){
        new SendGroupInvServer(allFriendsNames).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

//class checkConnection extends AsyncTask<Void, Void, Boolean>{
//    @Override
//    protected Boolean doInBackground(Void... voids) {
//        try {
//            Network.instance.oos.writeObject(Command.checkConnection);
//            Network.instance.oos.flush();
//
//            if(!Network.instance.ois.readObject().equals("success")){
//                Network.instance.Start();
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}

class SyncFriendsIPs extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... s) {
        synchronized(Network.instance.NetLock) {
            try {
                Network.instance.oos.writeObject(Command.sendFriends);
                Network.instance.oos.flush();
                Network.instance.oos.writeObject(User.mainUser.getFriendList());
                Network.instance.oos.flush();
                User.mainUser.setFriendList((Hashtable<String, Friend>) Network.instance.ois.readObject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

class ListenToMessages extends AsyncTask<String, MessagePacket, Void> {
    @Override
    protected Void doInBackground(String... s) {

        while (true)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Network.instance.NetLock) {
                try {
                    Network.instance.socket.setSoTimeout(10);
                    Log.v("XXX", "LISTEN TO MSGS");

                    //
                    if ( Network.instance.ois.readObject() == Command.createNewGroup)
                    {
                        Log.v("XXX","22222222222222222222222222222222222");
                        GroupListFrag.RefreshRecyclerView(new FriendGroup( ( ArrayList<Friend>) Network.instance.ois.readObject() ));
                    } else
                    {
                        MessagePacket mp = (MessagePacket) Network.instance.ois.readObject();
                        //Log.v("XXX", "MSG: " + mp.getText() + " From " + mp.getSender());
                        publishProgress(mp);
                    }

                } catch (Exception e) {
                } finally {
                    try {
                        Network.instance.socket.setSoTimeout(0);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(MessagePacket... msgs) {
        MainActivity.chatHistory.get(msgs[0].getSender()).add(msgs[0]);
        ChatActivity.NotifyDataSetChange();
        MainActivity.SaveChatHistory();
    }
}

class SendMSG extends AsyncTask<String, Void, Void> {

    String friendName, text;

    public SendMSG(String friendName,String text){
        this.friendName = friendName;
        this.text = text;
    }

    @Override
    protected Void doInBackground(String... s) {

        synchronized(Network.instance.NetLock) {

            Log.v("XXX","SENDING MSG");

            try {
                MessagePacket mp = new MessagePacket(User.mainUser.getUsername(),friendName,text);

                Network.instance.oos.writeObject(Command.sendMsg);
                Network.instance.oos.flush();

                Network.instance.oos.writeObject(mp);
                Network.instance.oos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

class SendGroupInvServer extends AsyncTask<String, Void, Void> {

    private ArrayList<String> allFriendsNames;

    public SendGroupInvServer(ArrayList<String> allFriendsNames){
        this.allFriendsNames = allFriendsNames;
    }

    @Override
    protected Void doInBackground(String... s) {

        synchronized(Network.instance.NetLock) {

            try {
                Network.instance.oos.writeObject(Command.createNewGroup);
                Network.instance.oos.flush();

                Network.instance.oos.writeObject(allFriendsNames);
                Network.instance.oos.flush();

                //reset
                MainActivity.allChosenFriendsGroup.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}