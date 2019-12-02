package com.leyouxianggou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    PRICE_NOT_NULL(200,"123")
    ;
    private Integer code;
    private String msg;
}