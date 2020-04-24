package com.leyouxianggou.order.config;

import com.leyouxianggou.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {

    @Autowired
    private IdWorkerProperties idWorkerProperties;

    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker(idWorkerProperties.getWorkerId(),idWorkerProperties.getDatacenterId());
    }
}
