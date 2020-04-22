package com.leyouxianggou.order.config;

import com.leyouxianggou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Component
@ConfigurationProperties(prefix = "ly.jwt")
@Data
public class JwtProperties {
    private String publicKeyPath;
    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    private void initKey() throws Exception{
        this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }
}
