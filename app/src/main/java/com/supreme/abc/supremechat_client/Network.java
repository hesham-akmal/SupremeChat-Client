package com.supreme.abc.supremechat_client;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network_data.Command;

public class Network {

    public static Network instance = new Network();

    //public String MainServerIP = "supremechatserver1.ddns.net"; //Hesham
    public static String MainServerIP = "supremechat.ddns.net"; //Grey

    public int MainServerPORT = 3000;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public boolean sendHeartbeat;
    private AlertDialog builder;
    private Runnable HeartbeatHandlerRunnable;

    public void SetAlertDialogContext(Context context) {
        builder = new AlertDialog.Builder(context).create();
        builder.setTitle("Cannot connect to server");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
    }

    public boolean Start() {
        try {
            socket = new Socket(MainServerIP, MainServerPORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            sendHeartbeat = true;
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally{
        }
    }

    public void StartHeartbeatService(){

        //If already initialized, resume thread
        if(HeartbeatHandlerRunnable != null){
            //HeartbeatHandlerRunnable.notify();
            if(!sendHeartbeat)
                sendHeartbeat=true;
            return;
        }

        //If not initialized, create and start
        final Handler HeartbeatTimerHandler = new Handler();
        HeartbeatHandlerRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    if(sendHeartbeat)
                    {
                        oos.writeObject(Command.heartbeat);
                        oos.flush();
                        Log.v("XXX", "SENT HEARTBEAT");
                    }
                    else
                        Log.v("XXX", "HEARTBEAT STOPPED");

                }catch (Exception e){
                    Log.v("XXX","LOST CONNECTION");
                }finally {
                    //run each second
                    HeartbeatTimerHandler.postDelayed(this, 1000);
                }
            }
        };
        HeartbeatHandlerRunnable.run();
    }

    public void StopHeartbeatService(){
        sendHeartbeat = false;
        /*try {
            HeartbeatHandlerRunnable.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
