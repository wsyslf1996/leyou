package com.leyouxianggou.sms.service;

public interface SmsService {
    void sendMessage(String phoneNum,String verifyCode);

    void sendMessage(String phoneNum);
}
