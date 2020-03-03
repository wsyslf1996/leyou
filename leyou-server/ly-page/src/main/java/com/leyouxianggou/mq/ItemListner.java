package com.leyouxianggou.mq;

import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.service.StaticPageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListner {

    @Autowired
    private StaticPageService staticPageService;

    /**
     * 监听到新增或者修改，则新建或者重建商品详情页
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.itemdetail.insert.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {LYMQRoutingKey.ITEM_INSERT,
                    LYMQRoutingKey.ITEM_UPDATE})) // 此key为RoutingKey
    public void listenItemInsertOrUpdate(String msg){
        long spuId = Long.parseLong(msg);
        staticPageService.createHtml(spuId);
    }

    /**
     * 监听到删除信息，删除商品详情页
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.itemdetail.delete.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {LYMQRoutingKey.ITEM_DELETE}))  // 此key为RoutingKey
    public void listenItemDelete(String msg){
        long spuId = Long.parseLong(msg);
        staticPageService.deleteHtml(spuId);
    }
}
