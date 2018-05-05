package com.supreme.abc.supremechat_client.Networking;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

//import com.supreme.abc.supremechat_client.MainActivity;
//import com.supreme.abc.supremechat_client.MessageContainer;
import com.supreme.abc.supremechat_client.FriendChat.ChatActivity;
import com.supreme.abc.supremechat_client.GroupChat.FriendGroup;
import com.supreme.abc.supremechat_client.GroupChat.GroupChatActivity;
import com.supreme.abc.supremechat_client.GroupChat.GroupListFrag;
import com.supreme.abc.supremechat_client.MainActivity;
import com.supreme.abc.supremechat_client.MyApplication;
import com.supreme.abc.supremechat_client.R;
import com.supreme.abc.supremechat_client.User;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import network_data.Command;
import network_data.Friend;
import network_data.MessagePacket;

public class AsyncTasks {

    public static void SyncFriendsIPs() {
        new SyncFriendsIPs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /*public static void startListeningToClients(){
        //IN CASE WE'LL USE SAME NETWORK //new ListenToClients().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/

    public static void SendMSGtoClient(String friendName, String text) {
        //IN CASE WE'LL USE SAME NETWORK  //new FriendClient(IP,text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SendMSG(friendName, text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void SendGroupMSGtoClient(List<String> friendNames, String text) {
        //IN CASE WE'LL USE SAME NETWORK  //new FriendClient(IP,text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SendGroupMSG(friendNames, text).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

//    public static void CheckConnection(){
//        new checkConnection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }

    public static void ListenToMessages() {
        new ListenToMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void SendGroupInvServer(ArrayList<String> allFriendsNames) {
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
        synchronized (Network.instance.NetLock) {
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

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Network.instance.NetLock) {
                try {

                    Network.instance.socket.setSoTimeout(10);
                    //Log.v("XXX", "LISTEN TO MSGS");


                    Command c = (Command) Network.instance.ois.readObject();
                    if (c == Command.createNewGroup) {
                        Log.v("XXX", "GROUP CREATED RECEIVED");
                        FriendGroup friendGroup = new FriendGroup((ArrayList<Friend>) Network.instance.ois.readObject());

                        if (!GroupListFrag.allFriendGroupsNames.contains(friendGroup.getFriendGroupName())) {
                            User.mainUser.AddFriendGroup(friendGroup.getFriendGroupName(), friendGroup);
                            MainActivity.friendGroups.add(friendGroup);
                            GroupListFrag.RefreshRecyclerView(friendGroup);
                        }

                        //GroupListFrag.RefreshRecyclerView(friendGroup);

                        MainActivity.groupChatHistory.put(friendGroup.getFriendGroupName(), new ArrayList<>());
                        MainActivity.SaveGroupChatHistory();

                    } else if (c == Command.sendMsg) {
                        Log.v("XXX", "GROUP MSG RECEIVED");
                        MessagePacket mp = (MessagePacket) Network.instance.ois.readObject();
                        Log.v("XXX", "MSG: " + mp.getText() + " From " + mp.getSender());
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

        if (!msgs[0].IsGroupMSG()) {
            MainActivity.chatHistory.get(msgs[0].getSender()).add(msgs[0]);
            ChatActivity.NotifyDataSetChange();
            MainActivity.SaveChatHistory();
        } else if (msgs[0].IsGroupMSG()) {

            String title = "";
            for (String s : msgs[0].getListOfRecievers())
                title += s + " - ";

            if (!MainActivity.groupChatHistory.containsKey(title)) {
                Log.v("XXX", "MSG FOR NEW GROUP");
                MainActivity.groupChatHistory.put(title, new ArrayList<>());
                MainActivity.groupChatHistory.get(title).add(msgs[0]);
                GroupChatActivity.NotifyDataSetChange();
                MainActivity.SaveGroupChatHistory();
            } else {
                Log.v("XXX", "MSG FOR OLD GROUP");
                MainActivity.groupChatHistory.get(title).add(msgs[0]);
                GroupChatActivity.NotifyDataSetChange();
                MainActivity.SaveGroupChatHistory();
            }

        }
    }
}

class SendMSG extends AsyncTask<String, Void, Void> {

    String friendName, text;

    public SendMSG(String friendName, String text) {
        this.friendName = friendName;
        this.text = text;
    }

    @Override
    protected Void doInBackground(String... s) {

        synchronized (Network.instance.NetLock) {

            Log.v("XXX", "SENDING MSG");

            try {
                MessagePacket mp = new MessagePacket(User.mainUser.getUsername(), friendName, text, false);

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

class SendGroupMSG extends AsyncTask<String, Void, Void> {

    String text;
    List<String> friendNames;

    public SendGroupMSG(List<String> friendNames, String text) {
        this.friendNames = friendNames;
        this.text = text;
    }

    @Override
    protected Void doInBackground(String... s) {

        synchronized (Network.instance.NetLock) {

            Log.v("XXX", "SENDING MSG");

            try {
                MessagePacket gmp = new MessagePacket(User.mainUser.getUsername(), friendNames, text, true);

                Network.instance.oos.writeObject(Command.sendGroupMsg);
                Network.instance.oos.flush();

                Network.instance.oos.writeObject(gmp);
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

    public SendGroupInvServer(ArrayList<String> allFriendsNames) {
        this.allFriendsNames = allFriendsNames;
    }

    @Override
    protected Void doInBackground(String... s) {

        synchronized (Network.instance.NetLock) {

            Log.v("XXX", "SENDING GROUP INV");

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