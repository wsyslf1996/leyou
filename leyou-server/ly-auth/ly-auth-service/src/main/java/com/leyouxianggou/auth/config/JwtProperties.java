package com.leyouxianggou.auth.config;

import com.leyouxianggou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
@ConfigurationProperties(prefix = "ly.jwt")
@Data
public class JwtProperties {
    private String secret;
    private String privateKeyPath;
    private String publicKeyPath;
    private int expire;
    private String cookieName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    private void initKey() throws Exception{
        // 如果没有公钥私钥，就生成一对公钥私钥
        if(!new File(privateKeyPath).exists() || !new File(publicKeyPath).exists()){
            RsaUtils.generateKey(publicKeyPath,privateKeyPath,secret);
        }
        this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }
}
