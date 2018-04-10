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
        if (passwordText.getText().toString().equals(passwordConfirmText.getText().toString())) {
//            new SignUpConnection().execute();
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(i);
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
                socket = new Socket(LoginActivity.MainServerIP, LoginActivity.MainServerPORT);
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
}
