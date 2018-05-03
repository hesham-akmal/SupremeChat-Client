package com.supreme.abc.supremechat_client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supreme.abc.supremechat_client.Networking.Network;

import network_data.AuthUser;
import network_data.Command;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameText, passwordText, passwordConfirmText;
    private TextView errorPassword,signup_link;
    private Button btn;
    private FloatingActionButton fab_back ;

    private SharedPreferences prefs ;
    private SharedPreferences.Editor editor;
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
        passwordConfirmText = findViewById(R.id.confirm_password_text);

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btn.performClick();
                }
                return false;
            }
        });

        passwordConfirmText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btn.performClick();
                }
                return false;
            }
        });

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
        SetSignInState(null);

        if(checkLoggedIn())
            StartLoginLoadingScreen();

        if( Network.instance.Start() )
            if(checkLoggedIn())
                autoLoginIn();
    }
    private void autoLoginIn(){
        synchronized(Network.instance.NetLock) {
            try {
                String username = prefs.getString("username", null);
                Network.instance.oos.writeObject(Command.signInAuto);
                Network.instance.oos.flush();
                Network.instance.oos.writeObject(new AuthUser(username, null));
                Network.instance.oos.flush();
                User.mainUser.Create(username);
                StartChatActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean checkLoggedIn() {
        //Check if logged in before ("checkLoggedIn" in sharedPrefs)
        return ( prefs.getBoolean("checkLoggedIn",false) && prefs.getString("username",null)!=null );
    }

    public void SetSignInState(View view){
        passwordConfirmText.setVisibility(View.GONE);
        fab_back.setVisibility(View.GONE);
        errorPassword.setVisibility(View.GONE);
        signup_link.setVisibility(View.VISIBLE);
        btn.setText("LOGIN");
        ActivityState = State.SIGN_IN;
        StopLoginLoadingScreen();
    }
    public void setSignUpState(View view){
        passwordConfirmText.setVisibility(View.VISIBLE);
        fab_back.setVisibility(View.VISIBLE);
        signup_link.setVisibility(View.GONE);
        btn.setText("SIGN UP");
        ActivityState = State.SIGN_UP;
        StopLoginLoadingScreen();
    }

    //Clicking button
    public void BtnClick(View view) {
        errorPassword.setVisibility(View.GONE);
        StartLoginLoadingScreen();
        new ClientLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void StartLoginLoadingScreen(){
        dialog.setMessage("Logging in");
        dialog.show();
    }
    private void StopLoginLoadingScreen(){
        dialog.cancel();
    }

    public class ClientLogin extends AsyncTask<Integer, Void, Integer> {

        //.execute() runs code below
        @Override
        protected Integer doInBackground(Integer... integers) {

            synchronized (Network.instance.NetLock) {

                    //if sign up and two pass doesnt match
                    if (ActivityState == State.SIGN_UP &&
                            !passwordText.getText().toString().equals(passwordConfirmText.getText().toString()))
                        return -4;

                    try {

                        String username = usernameText.getText().toString();
                        //encrypt password to be sent
                        String password = AdvancedCrypto.encrypt(passwordText.getText().toString());

                        switch (ActivityState) {
                            case SIGN_IN:

                                //send signIn command
                                Network.instance.oos.writeObject(Command.signIn);
                                Network.instance.oos.flush();

                                //send username and pass to server
                                Network.instance.oos.writeObject(new AuthUser(username, password));
                                Network.instance.oos.flush();


                                command = (Command) Network.instance.ois.readObject();
                                //Success. Username found and pass correct
                                if (command == Command.success) {
                                    User.mainUser.Create(username);

                                    editor.putBoolean("checkLoggedIn", true);
                                    editor.putString("username", username);
                                    editor.apply();

                                    return 1;
                                }
                                //Fail. Username does not exists, or found but incorrect pass
                                else if (command == Command.fail) {
                                    return -1;
                                }

                                break;

                            case SIGN_UP:

                                //send signUp command
                                Network.instance.oos.writeObject(Command.signUp);
                                Network.instance.oos.flush();

                                //send username and pass
                                Network.instance.oos.writeObject(new AuthUser(username, password));
                                Network.instance.oos.flush();

                                command = (Command) Network.instance.ois.readObject();
                                //Username doesn't already exist, user created successfully
                                if (command == Command.success) {
                                    User.mainUser.Create(username);

                                    editor.putBoolean("checkLoggedIn", true);
                                    editor.putString("username", username);
                                    editor.apply();

                                    return 1;
                                }
                                //Username already exists
                                else if (command == Command.fail) {
                                    return -3;
                                }

                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                SetSignInState(null);
                break;
            case -3:
                Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_LONG).show();
                setSignUpState(null);
                break;
            case -4:
                //errorPassword.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
                //setSignUpState(null);
                break;
            case 0:
                Toast.makeText(getApplicationContext(), "Unknown error has occured. Please try again.", Toast.LENGTH_LONG).show();
                SetSignInState(null);
                break;
        }
    }
}

    private void StartChatActivity() {
        //startActivity(new Intent(getApplicationContext(), ChatListActivity.class));
        startActivity(new Intent(getApplicationContext(), ChatListActivity.class));
        StopLoginLoadingScreen();
        finish();
    }

}
