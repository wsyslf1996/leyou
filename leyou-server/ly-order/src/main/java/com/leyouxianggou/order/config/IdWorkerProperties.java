package com.leyouxianggou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ly.idworker")
public class IdWorkerProperties {
    private Long workerId;
    private Long datacenterId;
}
