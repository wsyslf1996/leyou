package com.leyouxianggou.user.service.impl;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.user.mapper.UserMapper;
import com.leyouxianggou.user.pojo.User;
import com.leyouxianggou.user.service.UserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Boolean checkData(String data, Integer dataType) {
        User user = new User();
        switch (dataType){
            // 校验账号
            case 1:
                user.setUsername(data);
                break;
            // 校验手机号
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.UNDEFINED_DATA_TYPE);
        }
        int result = userMapper.selectCount(user);
        return result != 1;
    }

    @Override
    public void verifyPhoneNum(String phoneNum) {
        amqpTemplate.convertAndSend(LYMQRoutingKey.CHECK_PHONE,phoneNum);
    }

    @Override
    public void register(User user, String code) {
        // 校验验证码

        // 校验用户名

        // 加密密码

        // 写入数据库
    }
}
