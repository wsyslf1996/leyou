package com.leyouxianggou.order.service;

import com.leyouxianggou.order.dto.OrderDTO;

import java.util.Map;

public interface OrderService {
    Map<String,String> createOrder(OrderDTO orderDTO);
}
