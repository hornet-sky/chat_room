package com.wh.entity;

import org.springframework.web.socket.WebSocketMessage;

public class TextMessage implements WebSocketMessage<String> {
    private String payload;
    public TextMessage(String payload) {
        this.payload = payload;
    }
    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public int getPayloadLength() {
        return payload == null ? 0 : payload.length();
    }

    @Override
    public boolean isLast() {
        return true;
    }

}
