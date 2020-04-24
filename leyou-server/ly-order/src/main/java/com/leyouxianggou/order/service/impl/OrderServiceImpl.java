package com.leyouxianggou.order.service.impl;

import com.leyouxianggou.auth.entity.UserInfo;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.common.utils.IdWorker;
import com.leyouxianggou.item.Sku;
import com.leyouxianggou.item.CartDTO;
import com.leyouxianggou.order.client.GoodsClient;
import com.leyouxianggou.order.dto.OrderDTO;
import com.leyouxianggou.order.enums.OrderStatusEnums;
import com.leyouxianggou.order.interceptor.UserInterceptor;
import com.leyouxianggou.order.mapper.OrderDetailMapper;
import com.leyouxianggou.order.mapper.OrderMapper;
import com.leyouxianggou.order.mapper.OrderStatusMapper;
import com.leyouxianggou.order.pojo.Order;
import com.leyouxianggou.order.pojo.OrderDetail;
import com.leyouxianggou.order.pojo.OrderStatus;
import com.leyouxianggou.order.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Override
    @Transactional
    public Map<String,String> createOrder(OrderDTO orderDTO) {

        //生成订单ID
        Long orderID = idWorker.nextId();
        // 支付类型
        Integer paymentType = orderDTO.getPaymentType();
        // 收件人ID
        Long receiverId = orderDTO.getReceiverId();

        // 查询所有商品参数
        List<CartDTO> carts = orderDTO.getCarts();
        Map<Long,Integer> cartMap = carts.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        List<Long> skuIds = new ArrayList<>(cartMap.keySet());
        List<Sku> skus = goodsClient.querySkuListByIds(skuIds);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        long totalPay = 0L;
        for (Sku sku : skus) {
            // 计算总金额
            totalPay+=sku.getPrice()*cartMap.get(sku.getId());
            // 准备OrderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderID);
            orderDetail.setSkuId(sku.getId());
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            orderDetail.setNum(cartMap.get(sku.getId()));
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setSpec(sku.getOwnSpec());
            orderDetail.setTitle(sku.getTitle());
            orderDetailList.add(orderDetail);
        }
        // 查询用户信息
        UserInfo user = UserInterceptor.getUserInfo();

        // 创建订单信息
        Order order = new Order();
        order.setOrderId(orderID);
        order.setPaymentType(paymentType);
        order.setReceiverId(receiverId);
        order.setTotalPay(totalPay);
        order.setActualPay(totalPay); // 这里以后如果有邮费，需要加上邮费减去优惠
        order.setUserId(user.getId());

        // 保存到数据库
        int count = orderMapper.insert(order);
        if(count!=1){
            throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
        }

        // 创建订单详情
        count = orderDetailMapper.insertList(orderDetailList);
        if(count!=orderDetailList.size()){
            throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
        }

        // 创建订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderID);
        orderStatus.setStatus(OrderStatusEnums.UN_PAY.value());
        orderStatus.setCreateTime(new Date());
        count = orderStatusMapper.insert(orderStatus);
        if(count!=1){
            throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
        }

        // 减库存
        goodsClient.decreaseStock(carts);

        // 返回订单ID
        System.out.println("订单创建成功，订单号:"+orderID);
        Map<String,String> orderMap = new HashMap<>();
        orderMap.put("orderId",orderID.toString());
        return orderMap;
    }
}
