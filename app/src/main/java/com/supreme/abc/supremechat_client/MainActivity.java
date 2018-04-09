package com.supreme.abc.supremechat_client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button btn;

    public String MainServerIP = "197.52.21.251" ; //HeshamPC = 197.52.21.251
    public int MainServerPORT = 8109 ;

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
                socket = new Socket(MainServerIP,MainServerPORT );
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(tv.getText().toString());
                tv.setText("");
                out.flush();
            } catch (Exception e) {
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link button and edittext
        tv = findViewById(R.id.editText1);
        btn = findViewById(R.id.button1);
    }
}
