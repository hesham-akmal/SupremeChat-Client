package com.supreme.abc.supremechat_client;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import network_data.Command;

public class Network {

    public static Network instance = new Network();

    public String MainServerIP = "supremechatserver1.ddns.net"; //Hesham
    //public static String MainServerIP = "156.204.153.16"; //Grey&Ahmed
    public int MainServerPORT = 3000;

    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    private AlertDialog builder;

    public boolean sendHearbeats = true;

    public void SetAlertDialogContext(Context context) {
        builder = new AlertDialog.Builder(context).create();
        builder.setTitle("Cannot connect to server");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
    }

    public boolean Start() {
        try {
            //Change all socket connection liek this
            socket = new Socket();
            int timeout = 5000;
            socket.setSoTimeout(timeout);
            socket.connect(new InetSocketAddress(MainServerIP, MainServerPORT), timeout);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (Exception e)
        {
            return false;
        }
        finally{
            new startCheckingConnection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
    }

    public class startCheckingConnection extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            while(true)
            {
                try {
                    if(!sendHearbeats){
                        Thread.sleep(1000);//1 sec
                        continue;
                    }

                    oos.writeObject(Command.heartbeat);
                    oos.flush();

                    if( Network.instance.ois.readObject() == Command.heartbeat )
                    {
                        if(builder!=null)
                            builder.dismiss();
                    }
                } catch (Exception e) { //Not connected
                    Start();
                    return -1;
                }

                try {
                    Thread.sleep(2000);//1 sec
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == -1)
                if(!builder.isShowing())
                    builder.show();
        }
    }
}
