package com.leyouxianggou.sms.mq;

import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.common.utils.NumberUtils;
import com.leyouxianggou.sms.config.SmsProperties;
import com.leyouxianggou.sms.service.SmsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class SmsListner {

    @Autowired
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.user.check",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.sms.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {LYMQRoutingKey.CHECK_PHONE})) // 此key为RoutingKey
    public void sendMessage(String msg){
        smsService.sendVerifyMessage(msg);
    }
}
