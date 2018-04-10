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

public class LoginActivity extends AppCompatActivity {
    EditText usernameText, passwordText;
    Button btn;

    //public static String MainServerIP = "197.52.21.251"; //Hesham
    public static String MainServerIP = "156.204.165.39"; //Grey&Ahmed
    public static int MainServerPORT = 3000;

    //Clicking button
    public void BtnClick(View view) {
        new Client().execute();//connect and send text
    }

    private class Client extends AsyncTask<Integer, Void, Void> {
        private Socket socket;

        //.execute() runs code below
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                socket = new Socket(MainServerIP, MainServerPORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(usernameText.getText().toString());
                usernameText.setText("");
                out.flush();
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

    public void redirectView(View view) {
        Intent i = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(i);
    }

    //This should be called when the server authenticates the login info (user and pass are ok)
    public void StartMain(String a, String b, boolean admin){
        Intent intent = new Intent(getBaseContext(), Main.class);
        //Send user,pass,admin to next Activity
        intent.putExtra("username", a);
        intent.putExtra("password", b);
        intent.putExtra("admin", admin);
        startActivity(intent);
    }

}
