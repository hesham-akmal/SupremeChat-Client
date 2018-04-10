package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {
    EditText usernameText, passwordText;
    Button btn;

    //Clicking button
    public void BtnClick(View view) {

        //temp offline
        if (usernameText.getText().toString().equalsIgnoreCase("admin") && passwordText.getText().toString().equalsIgnoreCase("admin")) {
            StartChatActivity();
        } else {
            btn.setEnabled(false);
            new ClientLogin().execute();//connect and send text
        }
    }

    public class ClientLogin extends AsyncTask<Integer, Void, Void> {
        private Socket socket;

        //.execute() runs code below
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                /*
                -Send username,pass,ip to server, server authenticates
                 and returns success bool, if success: Return isAdmin and friendList
                 then serverside update IP,Status,lastLogin.
                */

                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String IP = socket.getInetAddress().getHostAddress();

                socket = new Socket(Network.MainServerIP, Network.MainServerPORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //must send login command too
                out.println(username + "," + password + "," + IP);
                out.flush();

                //if failed, enable login btn again
                //btn.setEnabled(true);

                //if success //server returns isAdmin,friendList //create mainUserObj
                boolean isAdmin = false;////implement
                Hashtable<String, String> friendList = null;////implement

                User.createMainUserObj(username,isAdmin,IP,friendList);
                StartChatActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //link button and edittext
        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        btn = findViewById(R.id.login_button);
    }

    public void StartSignUpActivity(View view) {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    private void StartChatActivity() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }

}
