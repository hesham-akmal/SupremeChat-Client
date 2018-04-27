package com.supreme.abc.supremechat_client.Networking;

import android.os.Handler;
import android.util.Log;

import com.network.mocket.MocketException;
import com.network.mocket.builder.client.Client;
import com.network.mocket.builder.client.ClientBuilder;
import com.network.mocket.builder.server.Server;
import com.network.mocket.builder.server.ServerBuilder;
import com.network.mocket.helper.Pair;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class RUDPClient {

    private int serverPort = 8095;
    private Handler MsgListenerTimerHandler;

    public RUDPClient() {

        try {
            ServerBuilder<byte []> serverBuilder = new ServerBuilder<byte []>()
                    .port(serverPort);
            final Server<byte[]> server = serverBuilder.build();

            MsgListenerTimerHandler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {

                    while (true) {
                        try
                        {
                            // blocking read
                            Pair<SocketAddress, byte[]> readBytes = server.read();
                            //Send read data to chat adapter or something
                            String readString = new String(readBytes.getSecond(), StandardCharsets.UTF_8);
                            Log.v("AAA",readString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            MsgListenerTimerHandler.postDelayed(r, 1000);

        } catch (MocketException e) {
            e.printStackTrace();
        }
    }

    public void SendMsg(String IP,String data){
        try {

            ClientBuilder<byte []> builder = new ClientBuilder<byte []>().host(IP, serverPort);
            Client<byte []> client = builder.build();
            client.write(data.getBytes());
            Log.v("AAA","Sending: " + data +" :to: " + IP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
