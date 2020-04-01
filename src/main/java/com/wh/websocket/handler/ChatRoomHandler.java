package com.wh.websocket.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.wh.entity.Message;
import com.wh.entity.User;
import com.wh.exception.UserNotFoundException;

@Component
public class ChatRoomHandler implements WebSocketHandler {
    private static Map<String, WebSocketSession> SESSION_MAP = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User joinedUser = getUser(session);
        System.out.println("afterConnectionEstablished -> " + joinedUser);
        SESSION_MAP.put(joinedUser.getId(), session); // 将session集中管理起来
        sendMessageToEveryone(joinedUser.getUsername() + " 进入了聊天室！");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> textMessage) throws Exception {
        System.out.println("handleMessage -> " + textMessage);
        if (textMessage.getPayloadLength() == 0) {
            return;
        }
        Message message  = JSON.parseObject(textMessage.getPayload().toString(), Message.class);
        String userId = message.getTo();
        if (userId == null || "".equals(userId)) {
            sendMessageToEveryone(message);
            return;
        }
        try {
            sendMessageTo(userId, message);
        } catch(Exception e) {
            e.printStackTrace();
            User joinedUser = getUser(session);
            sendMessageTo(joinedUser.getId(), e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        User joinedUser = getUser(session);
        System.out.println("handleTransportError --> " + joinedUser);
        sendMessageToEveryone(joinedUser.getUsername() + " 网络异常！");
        SESSION_MAP.remove(joinedUser.getId());
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        User joinedUser = getUser(session);
        System.out.println("afterConnectionClosed --> " + joinedUser);
        sendMessageToEveryone(joinedUser.getUsername() + " 离开了聊天室！");
        SESSION_MAP.remove(joinedUser.getId());
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        User user;
        for (WebSocketSession session : SESSION_MAP.values()) {
            if (session.isOpen()) {
                user = (User) session.getAttributes().get("joinedUser");
                if (user != null) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }

    private void sendMessageTo(String userId, TextMessage message) throws IOException {
        WebSocketSession session = SESSION_MAP.get(userId);
        if (session == null || !session.isOpen()) {
            throw new UserNotFoundException("用户（" + userId + "）已下线，消息未能送达");
        }
        session.sendMessage(message);
    }
    
    private void sendMessageTo(String userId, Message message) throws IOException {
        message.setUserList(this.getUserList());
        sendMessageTo(userId, new TextMessage(JSON.toJSONString(message)));
    }
    
    private void sendMessageTo(String userId, String content) throws IOException {
        Message message = new Message();
        message.setContent(content);
        message.setUserList(this.getUserList());
        sendMessageTo(userId, new TextMessage(JSON.toJSONString(message)));
    }
    
    private void sendMessageToEveryone(TextMessage message) throws IOException {
        for (WebSocketSession session : SESSION_MAP.values()) {
            if (session.isOpen()) {
                session.sendMessage(message);
            }
        }
    }
    
    private void sendMessageToEveryone(String content) throws IOException {
        Message message = new Message();
        message.setContent(content);
        message.setUserList(this.getUserList());
        sendMessageToEveryone(new TextMessage(JSON.toJSONString(message)));
    }
    
    private void sendMessageToEveryone(Message message) throws IOException {
        message.setUserList(this.getUserList());
        sendMessageToEveryone(new TextMessage(JSON.toJSONString(message)));
    }
    
    private User getUser(WebSocketSession session) {
        return (User) session.getAttributes().get("joinedUser");
    }
}
