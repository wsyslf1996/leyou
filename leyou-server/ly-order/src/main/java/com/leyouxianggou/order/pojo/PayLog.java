package com.leyouxianggou.order.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_pay_log")
@Data
public class PayLog {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "total_fee")
    private Long totalFee; // 总费用

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "pay_type")
    private Integer payType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "pay_time")
    private Date payTime;

    @Column(name = "closed_time")
    private Date closedTime;

    @Column(name = "refund_time")
    private Date refundTime;
}
