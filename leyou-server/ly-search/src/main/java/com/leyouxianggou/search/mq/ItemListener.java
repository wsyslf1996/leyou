package com.leyouxianggou.search.mq;

import com.leyouxianggou.common.enums.LYMQRoutingKey;
import com.leyouxianggou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {

    @Autowired
    private SearchService searchService;

    /**
     * 监听到新增或者修改，则修改索引库
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.insert.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {LYMQRoutingKey.ITEM_INSERT,
                    LYMQRoutingKey.ITEM_UPDATE,
                    LYMQRoutingKey.ITEM_ON_SHELVES})) // 此key为RoutingKey
    public void listenItemInsertOrUpdate(String msg){
        long spuId = Long.parseLong(msg);
        searchService.insertOrUpdateGoodsIndex(spuId);
    }

    /**
     * 监听到删除信息，删除索引库中的记录
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.delete.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {LYMQRoutingKey.ITEM_DELETE,
                    LYMQRoutingKey.ITEM_OFF_SHELVES}))  // 此key为RoutingKey
    public void listenItemDelete(String msg){
        long spuId = Long.parseLong(msg);
        searchService.deleteGoodsIndex(spuId);
    }
}
