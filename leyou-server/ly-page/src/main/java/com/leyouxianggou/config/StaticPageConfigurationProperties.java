package com.leyouxianggou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "ly.page")
public class StaticPageConfigurationProperties {

    /**
     * 静态页的路径
     */
    private String path;
}
