package com.wh.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Message {
    private String content;
    private String to;
    private String from;
    private String fromUsername;
    private long timestamp = System.currentTimeMillis();
    private List<User> userList = new ArrayList<>();
    public void addUser(User user) {
        userList.add(user);
    }
}
