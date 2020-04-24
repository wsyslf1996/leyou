package com.leyouxianggou.item.mapper;

import com.leyouxianggou.item.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock> {

    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock > #{num}")
    int decreaseStock(@Param("skuId") Long skuId,@Param("num")Integer num);
}
