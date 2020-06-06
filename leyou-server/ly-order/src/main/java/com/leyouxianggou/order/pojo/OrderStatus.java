package com.leyouxianggou.order.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_order_status")
@Data
public class OrderStatus {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    // 订单状态。1.未付款 2.已付款,未发货 3.已发货，未确认收货 4.确认收货，交易完成 5.交易关闭
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 订单创建时间

    @Column(name = "payment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentTime; // 支付时间

    @Column(name = "consign_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date consignTime;  // 发货时间

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime; // 交易完成时间

    @Column(name = "close_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime; // 订单关闭时间
}
