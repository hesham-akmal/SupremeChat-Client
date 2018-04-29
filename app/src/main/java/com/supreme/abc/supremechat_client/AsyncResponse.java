package com.supreme.abc.supremechat_client;

import network_data.MessagePacket;

public interface AsyncResponse {
    void processFinish(MessagePacket output);
}
