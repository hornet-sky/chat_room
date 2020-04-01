package com.wh.entity;

import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String gender;
    public User(String id, String username, String gender) {
        this.id = id;
        this.username = username;
        this.gender = gender;
    }
}
