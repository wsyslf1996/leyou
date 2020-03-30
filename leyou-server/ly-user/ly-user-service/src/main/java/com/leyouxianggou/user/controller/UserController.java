package com.leyouxianggou.user.controller;

import com.leyouxianggou.user.pojo.User;
import com.leyouxianggou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param data 需要校验的数据
     * @param dataType 需要校验的数据的类型。 1:账号  2:手机号
     * @return
     */
    @GetMapping("/check/{data}/{dataType}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable("data") String data,
            @PathVariable("dataType")Integer dataType){
        return ResponseEntity.ok(userService.checkData(data,dataType));
    }

    /**
     * 发送手机短信验证码
     * @param phoneNum
     * @return
     */
    @PostMapping("/verifycode")
    public ResponseEntity<Void> verifyPhoneNum(@RequestParam("phone")String phoneNum){
        userService.verifyPhoneNum(phoneNum);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(User user,String code){
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
