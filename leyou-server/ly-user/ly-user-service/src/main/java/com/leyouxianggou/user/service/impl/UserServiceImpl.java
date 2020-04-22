package com.leyouxianggou.user.service.impl;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.user.mapper.UserMapper;
import com.leyouxianggou.user.pojo.User;
import com.leyouxianggou.user.service.UserService;
import com.leyouxianggou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    // 短信验证码的key存入Redis的前缀
    private static final String PREFIX = "user:phone:verify:";

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
        String key = PREFIX + user.getPhone();
        BoundHashOperations<String, Object, Object> map = redisTemplate.boundHashOps(key);
        String cacheCode = map.get("code").toString();
        if(!StringUtils.equals(code,cacheCode)){
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }

        // 加密密码
        String password = user.getPassword();
        String salt = CodecUtils.generateSalt();
        String newPwd = CodecUtils.md5Hex(password, salt); // 密码最好混淆一下,MD5如今能够使用碰撞把密码碰撞出来

        // 写入数据库
        user.setSalt(salt);
        user.setPassword(newPwd);
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    @Override
    public User queryUserByUsernameAndPassword(String userName, String password) {
        User user=new User();
        user.setUsername(userName);
        User result = userMapper.selectOne(user); // 数据库中用户名加了索引,并且密码加密需要salt值，所以先根据用户名查用户
        // 校验用户名
        if(result == null){
            throw new LyException(ExceptionEnum.USER_NOT_FOUND);
        }
        // 校验密码
        if(!StringUtils.equals(result.getPassword(),CodecUtils.md5Hex(password,result.getSalt()))){
            LyException lyException = new LyException(ExceptionEnum.USER_NOT_FOUND);
            throw lyException;
        }
        return result;
    }
}
