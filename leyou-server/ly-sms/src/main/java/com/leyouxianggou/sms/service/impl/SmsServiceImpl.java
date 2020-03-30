package com.leyouxianggou.sms.service.impl;

import com.leyouxianggou.common.utils.NumberUtils;
import com.leyouxianggou.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    private static final String PREFIX = "user:verifycode:phone:";
    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;

    @Autowired
    private StringRedisTemplate template;

    @Override
    public void sendMessage(String phoneNum, String verifyCode) {
        // TODO 给手机发送验证码
        System.out.println(phoneNum+"的验证码为:"+verifyCode);

        // 将验证码存入Redis服务器
        template.opsForValue().set(PREFIX + phoneNum,verifyCode,5L, TimeUnit.MINUTES);
    }

    @Override
    public void sendMessage(String phoneNum) {
        sendMessage(phoneNum,NumberUtils.generateCode(DEFAULT_VERIFY_CODE_LENGTH));
    }
}
