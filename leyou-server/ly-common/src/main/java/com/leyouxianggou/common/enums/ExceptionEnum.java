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
    CATEGORY_UPDATE_ERROR(500,"更新商品种类信息失败"),

    SPEC_GROUP_NOT_FOUND(404,"规格组未查询到"),
    SPEC_GROUP_INSERT_ERROR(500,"新增参数规格组失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"更新参数规格组失败"),

    SPEC_PARAM_NOT_FOUND(404,"规格参数未查询到"),

    INVALID_FILE_TYPE(400,"无效的文件类型")
    ;
    private Integer code;
    private String msg;
}