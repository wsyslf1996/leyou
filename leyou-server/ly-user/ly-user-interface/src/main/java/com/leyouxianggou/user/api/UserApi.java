package com.leyouxianggou.user.api;

import com.leyouxianggou.user.pojo.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @PostMapping("/query")
    User queryUserByUsernameAndPassword(
            @RequestParam("username")String userName,
            @RequestParam("password")String password);
}
