package com.leyouxianggou.order.service;

import com.leyouxianggou.order.dto.OrderDTO;
import com.leyouxianggou.order.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map<String,String> createOrder(OrderDTO orderDTO);

    List<Order> querySelfOrder();
}
