package com.leyouxianggou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    BRAND_NOT_FOUND(404,"品牌数据未查询到"),
    BRAND_INSERT_ERROR(500,"品牌数据新增失败"),
    BRAND_UPDATE_ERROR(500,"品牌数据修改失败"),

    CATEGORY_NOT_FOUND(404,"商品分类数据未查询到"),
    CATEGORY_INSERT_ERROR(500,"新增商品分类失败"),
    CATEGORY_UPDATE_ERROR(500,"更新商品分类信息失败"),

    SPEC_GROUP_NOT_FOUND(404,"规格组未查询到"),
    SPEC_GROUP_INSERT_ERROR(500,"新增规格组失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"更新规格组失败"),

    SPEC_PARAM_NOT_FOUND(404,"规格参数未查询到"),
    SPEC_PARAM_INSERT_ERROR(500,"新增规格参数失败"),
    SPEC_PARAM_UPDATE_ERROR(500,"更新规格组失败"),

    INVALID_FILE_TYPE(400,"无效的文件类型"),

    GOODS_NOT_FOUND(404,"商品SPU信息未查询到"),
    ;
    private Integer code;
    private String msg;
}