package com.leyouxianggou.auth.controller;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.auth.service.AuthService;
import com.leyouxianggou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        String token = authService.login(username,password);
        // 将Token写入cookie
        CookieUtils.setCookie(request,response,cookieName,token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户的登录状态
     * @param token
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verifyToken(@CookieValue("t")String token){
        return ResponseEntity.ok(authService.verifyToken(token));
    }
}
