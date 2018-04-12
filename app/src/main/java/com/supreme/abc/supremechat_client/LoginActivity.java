package com.supreme.abc.supremechat_client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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

import java.io.IOException;
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

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Command command;

    ProgressDialog dialog;

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
        //create shared prefs vars
        editor = getSharedPreferences("ABC_key", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("ABC_key", MODE_PRIVATE);
        //create loading screen
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        //for showing and hiding btns
        setSignInState(null);

        if(checkKeepLoggedIn())
        {
            checkbox_keep.setChecked(true);
            StartLoginLoadingScreen();
        }

        connectToServer();
    }

    private void connectToServer(){
        //start loading screen
        new ConnectServer().execute();
    }

    private void autoLoginIn(){
        String username = prefs.getString("username",null);
        String IP = socket.getInetAddress().getHostAddress();
        User.createMainUserObj(username, IP);
        StartChatActivity();
    }

    private boolean checkKeepLoggedIn() {
        //Check if logged in before (the saved "keepMeLoggedIn" in sharedPrefs)
        if( prefs.getBoolean("keep",false) && prefs.getString("username",null)!=null )
            return true;
        else
            return false;
    }

    public void setSignInState(View view){
        passwordConfirmText.setVisibility(View.GONE);
        fab_back.setVisibility(View.GONE);
        errorPassword.setVisibility(View.GONE);
        signup_link.setVisibility(View.VISIBLE);
        checkbox_keep.setVisibility(View.VISIBLE);
        btn.setText("LOGIN");
        ActivityState = State.SIGN_IN;
        StopLoginLoadingScreen();
    }
    public void setSignUpState(View view){
        passwordConfirmText.setVisibility(View.VISIBLE);
        fab_back.setVisibility(View.VISIBLE);
        signup_link.setVisibility(View.GONE);
        checkbox_keep.setVisibility(View.GONE);
        btn.setText("SIGN UP");
        ActivityState = State.SIGN_UP;
        StopLoginLoadingScreen();
    }

    //Clicking button
    public void BtnClick(View view) {
        errorPassword.setVisibility(View.GONE);
        StartLoginLoadingScreen();
        new ClientLogin().execute();//connect
    }

    private void StartLoginLoadingScreen(){
        dialog.setMessage("Logging in");
        dialog.show();
    }
    private void StopLoginLoadingScreen(){
        dialog.cancel();
    }

    public class ConnectServer extends AsyncTask<Integer, Void, Integer> {
        //.execute() runs code below
        @Override
        protected Integer doInBackground(Integer... integers) {
            try {
                socket = new Socket(Network.instance.MainServerIP, Network.instance.MainServerPORT);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override //Connected to server
        protected void onPostExecute(Integer result) {
            if(result != 1) //Connect failed
            {
                //Show error box////////
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                }
                builder.setTitle("Cannot connect to server")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
                /////////////////////////
                return;
            }

            //Connect success
            if(checkKeepLoggedIn())
                autoLoginIn();
        }
    }

    public class ClientLogin extends AsyncTask<Integer, Void, Integer> {

        //.execute() runs code below
        @Override
        protected Integer doInBackground(Integer... integers) {

            //if sign up and two pass doesnt match
            if (ActivityState == State.SIGN_UP &&
                    !passwordText.getText().toString().equals(passwordConfirmText.getText().toString()))
                return -4;

            try {

                String  username = usernameText.getText().toString();
                //encrypt password to be sent
                String password = AdvancedCrypto.encrypt(passwordText.getText().toString());
                String IP = socket.getInetAddress().getHostAddress();

                switch (ActivityState) {
                    case SIGN_IN:

                        //send signIn command
                        oos.writeObject(Command.signIn);
                        oos.flush();

                        //send username and pass to server
                        oos.writeObject(new AuthUser(username,password,IP));
                        oos.flush();

                        command = (Command) ois.readObject();
                        //Success. Username found and pass correct
                        if(command == Command.success)
                        {
                            User.createMainUserObj(username, IP);

                            //if keepMeLoggedIn, save "keepMeLoggedIn" and "username" to shared pref
                            boolean x = checkbox_keep.isChecked();
                            editor.putBoolean("keep", x);
                            if(x)
                            {
                                editor.putString("username", username);
                                editor.apply();
                            }
                            return 1;
                        }
                        //Fail. Username does not exists, or found but incorrect pass
                        else if(command == Command.fail)
                        {
                            return -1;
                        }

                        break;

                    case SIGN_UP:

                        //send signUp command
                        oos.writeObject(Command.signUp);
                        oos.flush();

                        //send username and pass
                        oos.writeObject(new AuthUser(username,password,IP));
                        oos.flush();

                        command = (Command) ois.readObject();
                        //Username doesn't already exist, user created successfully
                        if(command == Command.success)
                        {
                            User.createMainUserObj(username, IP);
                            return 1;
                        }
                        //Username already exists
                        else if (command == Command.fail)
                        {
                            return -3;
                        }

                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
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
    }

    private void StartChatActivity() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        StopLoginLoadingScreen();
        finish();
    }

}
