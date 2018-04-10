package com.supreme.abc.supremechat_client;

import android.os.Bundle;
import android.widget.ImageView;

public class ChatActivity extends StatusHandler {

    ImageView senderPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
