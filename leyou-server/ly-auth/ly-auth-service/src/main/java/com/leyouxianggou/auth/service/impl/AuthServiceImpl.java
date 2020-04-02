package com.leyouxianggou.auth.service.impl;

import com.leyouxianggou.auth.client.UserClient;
import com.leyouxianggou.auth.config.JwtProperties;
import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.auth.service.AuthService;
import com.leyouxianggou.auth.utils.JwtUtils;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserClient userClient;

    /**
     *
     * @param username
     * @param password
     * @return 登录成功返回Token值
     */
    @Override
    public String login(String username, String password) {
        User user = userClient.queryUserByUsernameAndPassword(username, password);
        if(user == null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 登录成功，用私钥加密
        String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        return token;
    }

    @Override
    public UserInfo verifyToken(String token) {
        if(StringUtils.isBlank(token)){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        try{
            UserInfo userInfo = JwtUtils.getUserInfo(jwtProperties.getPublicKey(), token);
            return userInfo;
        }catch (Exception e){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }
}
