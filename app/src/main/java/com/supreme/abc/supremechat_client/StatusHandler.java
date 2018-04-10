package com.supreme.abc.supremechat_client;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * This class updates user Status and LastLogin
 */

public class StatusHandler extends AppCompatActivity {

    /////IDLE HANDLER CODE /////////////////////////////////////////////////////////////////////////
    //Immediately after user stops touching screen, timer starts counting until 1 minute goes by, after that run() will run.
    //When user starts interaction and stops again, timer will start again.

    public static final long DISCONNECT_TIMEOUT = 60000; // 1 min = 1 * 60 sec * 1000 ms

    private static Handler disconnectHandler = new Handler();

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            //Set Status to idle
            User.mainUser.setIdleStatus();
            //Toast.makeText(getApplicationContext(), "idle for 1 min", Toast.LENGTH_SHORT).show();
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    ///////// /////////////// /////////////////////////////////////////////////////////////////////
  /*
        Commented out.
        LastLogin Time should be implemented in the server instead, for better client performance.

    //LastLogin HANDLER CODE//////////////////////////
    //run() runs every one minute.

    //runs without a timer by reposting this handler at the end of the runnable
    Handler LastLoginTimerHandler = new Handler();
    Runnable LastLoginRunnable = new Runnable() {

        @Override
        public void run() {
            ChatActivity.loggedUser.updateLastLoginAndSync();
            //Toast.makeText(getApplicationContext(), "60 sec went by", Toast.LENGTH_SHORT).show();
            LastLoginTimerHandler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LastLoginTimerHandler.removeCallbacks(LastLoginRunnable);
    }
    //////////////////////////////////////////////*/

}