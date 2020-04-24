package com.leyouxianggou.item;

import lombok.Data;

@Data
public class CartDTO {
    private Long skuId; //商品sku Id
    private Integer num; // 当前sku的数量
}
