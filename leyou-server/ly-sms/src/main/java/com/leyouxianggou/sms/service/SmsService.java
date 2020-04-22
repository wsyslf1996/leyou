package com.leyouxianggou.sms.service;

import java.util.Map;

public interface SmsService {

    void sendVerifyMessage(String phoneNum,String verifyCode);

    void sendVerifyMessage(String phoneNum);
}
