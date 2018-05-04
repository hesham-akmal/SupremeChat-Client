package com.supreme.abc.supremechat_client.Networking;

import android.os.Handler;
import android.util.Log;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network_data.Command;

public class Network {

    public static Network instance = new Network();

    //public String MainServerIP = "supremechatserver1.ddns.net"; //Hesham
    public static String MainServerIP = "chat.ddns.net"; //Grey

    public int MainServerPORT = 3000;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    private  boolean sendHeartbeats;
    private Runnable HeartbeatHandlerRunnable;
    private Handler HeartbeatTimerHandler;

    public final Object NetLock = new Object();

    public boolean Start() {
        try {
            socket = new Socket(MainServerIP, MainServerPORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            sendHeartbeats = true;

            HeartbeatTimerHandler = new Handler();

            HeartbeatHandlerRunnable = new Runnable() {
                @Override
                public void run() {
                    synchronized(NetLock) {
                        try {
                            if(sendHeartbeats)
                            {
                                oos.writeObject(Command.heartbeat);
                                oos.flush();
                                //Log.v("XXX","SENT HB");
                            }
                        }catch (Exception e){
                            Log.v("XXX","LOST CONNECTION");
                        }finally {
                            //run each second
                            HeartbeatTimerHandler.postDelayed(this, 1000);
                        }
                    }
                }
            };
            //run the handler but don't actually start sending heartbeats yet
            sendHeartbeats = false;
            HeartbeatHandlerRunnable.run();

            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void StartHeartbeatService(){
        Log.v("XXX","RESUMED HB");
        sendHeartbeats = true;
    }

    public void StopHeartbeatService(){
        Log.v("XXX","STOPPED HB");
        sendHeartbeats = false;
    }
}
