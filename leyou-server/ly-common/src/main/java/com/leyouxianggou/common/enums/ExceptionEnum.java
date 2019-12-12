package com.leyouxianggou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    CATEGORY_NOT_FOUND(404,"商品分类数据没有查询到"),
    BRAND_NOT_FOUND(404,"品牌数据未查询到"),
    BRAND_INSERT_ERROR(500,"品牌数据新增失败");
    ;
    private Integer code;
    private String msg;
}