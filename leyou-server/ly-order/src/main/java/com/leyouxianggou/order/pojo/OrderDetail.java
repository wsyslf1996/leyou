package com.leyouxianggou.order.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_order_detail")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //订单详情ID

    @Column(name = "order_id")
    private Long orderId; // 订单ID

    @Column(name = "sku_id")
    private Long skuId; // 商品skuID

    @Column(name = "num")
    private Integer num; // 商品数量

    @Column(name = "title")
    private String title;// 商品标题

    @Column(name = "own_spec")
    private String spec; // 当前商品的参数

    @Column(name = "price")
    private Long price; // 商品价格

    @Column(name = "image")
    private String image; // 商品图片
}
