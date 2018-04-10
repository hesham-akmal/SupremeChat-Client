package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class SignupActivity extends AppCompatActivity {

    EditText usernameText, passwordText, passwordConfirmText;
    TextView errorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        passwordConfirmText = findViewById(R.id.confirm_password_text);
        errorPassword = findViewById(R.id.err_password);
    }

    public void SignUp(View view){
        //If 2 passwords match
        if (passwordText.getText().toString().equals(passwordConfirmText.getText().toString())) {
            //btn.setEnabled(false);
            new SignUpConnection().execute();
        }else{
            errorPassword.setVisibility(View.VISIBLE);
        }

    }

    private class SignUpConnection extends AsyncTask<Integer, Void, Void> {
        private Socket socket;

        //.execute() runs code below
        @Override
        protected Void doInBackground(Integer... integers) {
            try {

                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String IP = socket.getInetAddress().getHostAddress();

                socket = new Socket(Network.MainServerIP, Network.MainServerPORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //must send register command too
                out.println(username + "," + password + "," + IP );
                out.flush();

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

    private void StartChatActivity() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }
}
