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

    GOODS_DETAIL_NOT_FOUND(404,"商品详情信息未查询到"),
    GOODS_DETAIL_UPDATE_ERROR(500,"商品详情信息更新失败"),

    GOODS_SKU_NOT_FOUND(404,"商品SKU未查询到"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存未查询到"),

    GOODS_SPU_NOT_FOUND(404,"商品SPU信息未查询到"),
    GOODS_INSERT_ERROR(500,"新增商品失败"),
    GOODS_SPU_UPDATE_ERROR(500,"商品SPU更新失败"),
    GOODS_SPU_DETAIL_UPDATE_ERROR(500,"商品SPU Detail更新失败"),

    UNDEFINED_DATA_TYPE(404,"未定义的数据类型"),

    INVALID_VERIFY_CODE(500,"无效的验证码"),

    INVALID_USERNAME_PASSWORD(404,"无效的用户名和密码"),
    USER_NOT_FOUND(404,"未查询到用户信息"),
    UNAUTHORIZED(403,"无操作权限"),
    INVALID_TOKEN(500,"无效的Token")
    ;
    ;
    private Integer code;
    private String msg;
}