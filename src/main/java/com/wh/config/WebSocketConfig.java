package com.wh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.wh.websocket.handler.ChatRoomHandler;
import com.wh.websocket.interceptor.ChatRoomInterceptor;

@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ChatRoomInterceptor chatRoomInterceptor;
    @Autowired
    private ChatRoomHandler chatRoomHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRoomHandler, "/ws")
            .addInterceptors(chatRoomInterceptor)
            .setAllowedOrigins("*");
        registry.addHandler(chatRoomHandler, "/ws_sockjs")
            .addInterceptors(chatRoomInterceptor)
            .setAllowedOrigins("*")
            .withSockJS();
    }

}
