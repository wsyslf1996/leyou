package com.leyouxianggou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrderStatusEnums {

    UN_PAY(1,"未付款"),

    PAYED(2,"已付款，等待发货"),

    UN_CONFIRM(3,"未确认收货"),

    SUCCESS(4,"交易成功"),

    CLOSE(5,"交易失败，关闭交易")

    ;
    private Integer code;
    private String description;

    public int value(){
        return this.code;
    }
}
