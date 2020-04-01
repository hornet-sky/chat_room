package com.wh.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wh.entity.User;

@RestController
@CrossOrigin(allowCredentials = "true") // 允许跨域的凭证，比如cookie
@RequestMapping("/chat_room")
public class ChatRoomController {
    @GetMapping("/join")
    public Map<String, Object> join(String username, String gender, HttpServletRequest request) {
        System.out.println("username = " + username + ", gender = " + gender);
        User user = new User(UUID.randomUUID().toString(), username, gender);
        HttpSession session = request.getSession();
        session.setAttribute("joinedUser", user);
        return generateSuccessResult(user);
    }
    private Map<String, Object> generateSuccessResult(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("message", "success");
        result.put("data", data);
        return result;
    }
    @SuppressWarnings("unused")
    private Map<String, Object> generateSuccessResult() {
        return generateSuccessResult(null);
    }
}
