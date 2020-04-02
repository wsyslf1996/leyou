package com.leyouxianggou.gateway.config;

import com.leyouxianggou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

@Component
@ConfigurationProperties(prefix = "ly.jwt")
@Data
public class JwtProperties {
    private String secret;
    private String publicKeyPath;
    private int expire;
    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    private void initKey() throws Exception{
        this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }
}
