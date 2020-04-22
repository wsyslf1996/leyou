package com.leyouxianggou.order.pojo;

import lombok.Data;

import javax.persistence.Table;

@Table(name = "tb_order")
@Data
public class Order {

    private Long orderId;
}
