package com.leyouxianggou.item;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stock")
@Data
public class Stock {
    @Id
    private Long skuId;
    private Integer stock;// 正常库存
}
