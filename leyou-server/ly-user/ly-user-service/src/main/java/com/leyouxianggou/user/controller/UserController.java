package com.leyouxianggou.user.controller;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.user.pojo.User;
import com.leyouxianggou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 用户注册
     * @param user
     * @param result
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, String code){
        // 先通过Valid注解进行校验，如果有错误就进入错误打印
        if(result.hasFieldErrors()){
            throw new RuntimeException(result.getFieldErrors()
                    .stream().map(e->e.getDefaultMessage()).collect(Collectors.joining("|")));
        }
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码进行查询
     * @return
     */
    @PostMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam("username")String userName,@RequestParam("password")String password){
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(userName,password));
    }
}
