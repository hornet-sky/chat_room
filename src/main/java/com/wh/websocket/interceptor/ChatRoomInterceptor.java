package com.wh.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.wh.entity.User;

@Component
public class ChatRoomInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest)) {
            return false;
        }
        HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession(false);
        User user = (User) session.getAttribute("joinedUser");
        if(user == null) {
            System.out.println("当前用户没有加入，无法握手");
            return false;
        }
        attributes.put("joinedUser", user);
        System.out.println("开始握手");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
        System.out.println("握手成功！");
    }

}
