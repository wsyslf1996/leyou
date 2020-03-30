package com.leyouxianggou.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    // TODO 引入jwt鉴权需要的配置属性
}
