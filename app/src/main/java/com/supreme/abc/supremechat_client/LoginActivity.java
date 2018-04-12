package com.supreme.abc.supremechat_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network_data.AuthUser;
import network_data.Command;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameText, passwordText, passwordConfirmText;
    private TextView errorPassword,signup_link;
    private Button btn;
    private FloatingActionButton fab_back ;
    private CheckBox checkbox_keep;

    private SharedPreferences prefs ;
    private SharedPreferences.Editor editor;

    private enum State{
        SIGN_IN,
        SIGN_UP
    }
    private State ActivityState;

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
        fab_back = findViewById(R.id.fab_back);
        checkbox_keep = (CheckBox) findViewById(R.id.checkbox_keep);
        passwordConfirmText = findViewById(R.id.confirm_password_text);
        errorPassword = findViewById(R.id.err_password);
        signup_link = findViewById(R.id.signup_link);
        //
        editor = getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("ABC_key", MODE_PRIVATE);

        setSignInState(null);

        checkKeepLoggedIn();
    }

    private void checkKeepLoggedIn() {
        //Check if logged in before and saved keepMeLoggedIn
        boolean x = prefs.getBoolean("keep",false);
        checkbox_keep.setChecked(x);
        if(x)
        {
            Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
            usernameText.setText(prefs.getString("username",null));
            passwordText.setText(prefs.getString("password",null));
            BtnClick(null);
        }
    }

    public void setSignInState(View view){
        passwordConfirmText.setVisibility(View.GONE);
        fab_back.setVisibility(View.GONE);
        signup_link.setVisibility(View.VISIBLE);
        checkbox_keep.setVisibility(View.VISIBLE);
        signup_link.setEnabled(true);
        usernameText.setEnabled(true);
        passwordText.setEnabled(true);
        btn.setText("LOGIN");
        btn.setEnabled(true);
        checkbox_keep.setEnabled(true);
        errorPassword.setVisibility(View.GONE);
        ActivityState = State.SIGN_IN;
    }
    public void setSignUpState(View view){
        passwordConfirmText.setVisibility(View.VISIBLE);
        fab_back.setVisibility(View.VISIBLE);
        signup_link.setVisibility(View.GONE);
        checkbox_keep.setVisibility(View.GONE);
        usernameText.setEnabled(true);
        passwordText.setEnabled(true);
        passwordConfirmText.setEnabled(true);
        btn.setText("SIGN UP");
        btn.setEnabled(true);
        ActivityState = State.SIGN_UP;
    }

    private void LoggingInState(){
        btn.setEnabled(false);
        usernameText.setEnabled(false);
        passwordText.setEnabled(false);
        passwordConfirmText.setEnabled(false);
        checkbox_keep.setEnabled(false);
        signup_link.setEnabled(false);
    }

    //Clicking button
    public void BtnClick(View view) {
        errorPassword.setVisibility(View.GONE);
        LoggingInState();
        new ClientLogin().execute();//connect
    }

    public class ClientLogin extends AsyncTask<Integer, Void, Integer> {
        private Socket socket;

        //.execute() runs code below
        @Override
        protected Integer doInBackground(Integer... integers) {

            //if sign up and two pass doesnt match
            if (ActivityState == State.SIGN_UP &&
                    !passwordText.getText().toString().equals(passwordConfirmText.getText().toString()))
                return -4;

            ObjectInputStream ois;
            ObjectOutputStream oos;
            Command command;
            try {
                socket = new Socket(Network.MainServerIP, Network.MainServerPORT);
                oos = new ObjectOutputStream(this.socket.getOutputStream());
                ois = new ObjectInputStream(this.socket.getInputStream());

                if(ActivityState == State.SIGN_IN){
                    //send signIn command
                    oos.writeObject(Command.signIn);

                    //wait for ack
                    command = (Command) ois.readObject();
                    if(command == Command.success)
                    {//server ready to receive

                        String  username = usernameText.getText().toString();
                        String password = passwordText.getText().toString();
                        String IP = socket.getInetAddress().getHostAddress();

                        //send username and pass to server
                        oos.writeObject(new AuthUser(username,password,IP));
                        oos.flush();

                        command = (Command) ois.readObject();

                        //Success. Username found and pass correct
                        if(command == Command.success)
                        {
                            User.createMainUserObj(username, IP);

                            //if keepMeLoggedIn, save user and pass to shared pref, along with bool for keepMeLoggedIn
                            boolean x = checkbox_keep.isChecked();
                            editor.putBoolean("keep", x);
                            if(x)
                            {
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.apply();
                            }
                            return 1;
                        }
                        //Fail. Username found but pass incorrect
                        else if(command == Command.fail)
                        {
                            return -1;
                        }
                    }
                    //Username does not exists
                    else if (command == Command.fail){//failed, enable login btn again
                        return -1;
                    }
                }
                else if(ActivityState == State.SIGN_UP){

                    //Sign up
                    //send command
                    oos.writeObject(Command.signUp);

                    //wait for ack
                    command = (Command) ois.readObject();
                    if(command == Command.success){
                        String username = usernameText.getText().toString();
                        String password = passwordText.getText().toString();
                        String IP = socket.getInetAddress().getHostAddress();

                        //send username and pass
                        oos.writeObject(new AuthUser(username,password,IP));
                        oos.flush();

                        command = (Command) ois.readObject();
                        //Username doesn't already exist, user created successfully
                        if(command == Command.success)
                        {
                            User.createMainUserObj(username, password);
                            return 1;
                        }
                        //Username already exists
                        else if (command == Command.fail)
                        {
                            return -3;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            switch (result) {
                case 1:
                    StartChatActivity();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "Username or Password incorrect.", Toast.LENGTH_LONG).show();
                    setSignInState(null);
                    break;
                case -3:
                    Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_LONG).show();
                    setSignUpState(null);
                    break;
                case -4:
                    errorPassword.setVisibility(View.VISIBLE);
                    setSignUpState(null);
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Unknown error has occured. Please try again.", Toast.LENGTH_LONG).show();
                    setSignInState(null);
                    break;
            }
        }

        private void StartChatActivity() {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            finish();
        }
    }
}
