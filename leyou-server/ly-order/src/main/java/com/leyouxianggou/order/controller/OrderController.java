package com.leyouxianggou.order.controller;

import com.alipay.api.AlipayApiException;
import com.leyouxianggou.order.dto.OrderDTO;
import com.leyouxianggou.order.pojo.Order;
import com.leyouxianggou.order.service.AlipayService;
import com.leyouxianggou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayService alipayService;

    @PostMapping("/business")
    @ResponseBody
    public ResponseEntity<Map<String,String>> createOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping("/business")
    public ResponseEntity<List<Order>> queryOrder(){
        return ResponseEntity.ok(orderService.querySelfOrder());
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<String> createAlipayOrder(@PathVariable("orderId") String orderId){
        return ResponseEntity.ok(alipayService.createDefaultAlipayOrder(orderId));
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handleNotify(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        return ResponseEntity.ok(alipayService.handleNotify(request));
    }
}
