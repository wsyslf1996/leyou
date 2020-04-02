package com.leyouxianggou.sms.service.impl;

import com.leyouxianggou.common.utils.NumberUtils;
import com.leyouxianggou.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    // 短信验证码的key存入Redis的前缀
    private static final String PREFIX = "user:phone:verify:";
    // 验证码长度
    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    // 短信发送频率 60s
    private static final long SMS_MIN_SEND_INTERVEL = 60 * 1000;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void sendMessage(String phoneNum, String verifyCode) {
        String key = PREFIX + phoneNum;
        BoundHashOperations<String, Object, Object> map = redisTemplate.boundHashOps(key);

        // 限流，防止恶意请求无线点短信
        if(map!=null && map.size() > 0){
            String time = map.get("time").toString();
            if(System.currentTimeMillis() - Long.valueOf(time) < SMS_MIN_SEND_INTERVEL){
                log.error("[短信服务]:短信发送频率过快，手机号码:{}",phoneNum);
                return ;
            }
        }

        // TODO 给手机发送验证码
        System.out.println(phoneNum+"的验证码为:"+verifyCode);

        // 将验证码存入Redis服务器
        map.put("code",verifyCode);
        map.put("time",String.valueOf(System.currentTimeMillis()));
        // 设置超时时长
        redisTemplate.expire(key,5,TimeUnit.MINUTES);
    }

    @Override
    public void sendMessage(String phoneNum) {
        sendMessage(phoneNum,NumberUtils.generateCode(DEFAULT_VERIFY_CODE_LENGTH));
    }
}
