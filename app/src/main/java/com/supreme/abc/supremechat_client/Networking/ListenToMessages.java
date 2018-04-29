package com.supreme.abc.supremechat_client.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.supreme.abc.supremechat_client.AsyncResponse;
import com.supreme.abc.supremechat_client.ChatActivity;

import java.net.SocketException;

import network_data.Command;
import network_data.MessagePacket;

public class ListenToMessages extends AsyncTask<String, MessagePacket, Void> {
    public AsyncResponse delegate = null;
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
                    Log.v("XXX", "LISTEN TO MSGS");
                    MessagePacket mp = (MessagePacket) Network.instance.ois.readObject();
                    Log.v("XXX", "MSG: " + mp.getText() + " From " + mp.getSender());
                    publishProgress(mp);
                    //new ListenToMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //starts listening again for next msg

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
        ChatActivity.messageList.add(msgs[0]);
        ChatActivity.messageAdapter.notifyDataSetChanged();

    }

//    @Override
//    protected void onPostExecute(MessagePacket mp) {
//
//        if (mp != null) {
//            delegate.processFinish(mp);
//        }
//    }
}