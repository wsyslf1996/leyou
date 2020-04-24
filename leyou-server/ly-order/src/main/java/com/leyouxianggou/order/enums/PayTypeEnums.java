package com.leyouxianggou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PayTypeEnums {

    ALI_PAY(1,"支付宝支付"),
    CASH_ON_DELIVERY(2,"货到付款")

    ;
    private Integer code;
    private String type;

    public Integer value(){
        return this.code;
    }
}
