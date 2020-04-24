package com.leyouxianggou.order.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_order")
@Data
public class Order {

    @Id
    @Column(name = "order_id")
    private Long orderId;  // 订单ID

    @Column(name = "total_pay")
    private Long totalPay; // 总金额，单位（分）

    @Column(name = "actual_pay")
    private Long actualPay; // 实际支付，单位（分）

    @Column(name = "payment_type")
    private Integer paymentType; // 支付类型

    @Column(name = "shipping_name")
    private String shippingName; // 物流名称

    @Column(name = "shipping_code")
    private String shippingCode; // 物流单号

    @Column(name = "user_id")
    private Long userId; // 用户ID

    @Column(name = "receiver_id")
    private Long receiverId; // 收件人ID

}
