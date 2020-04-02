package com.leyouxianggou.auth.service;

import com.leyouxianggou.auth.entity.UserInfo;

public interface AuthService {
    String login(String username, String password);

    UserInfo verifyToken(String token);
}
