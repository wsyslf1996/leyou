package com.leyouxianggou.user.service;

import com.leyouxianggou.user.pojo.User;

public interface UserService {
    Boolean checkData(String data, Integer dataType);

    void register(User user,String code);

    void verifyPhoneNum(String phoneNum);

    User queryUserByUsernameAndPassword(String userName, String password);
}
