package com.leyouxianggou.item.service;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.CartDTO;
import com.leyouxianggou.item.Sku;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.item.SpuDetail;

import java.util.List;

public interface GoodsService {
    PageResult<Spu> querySpuByPage(String key, Boolean saleable, int page, int pageSize);

    Spu querySpuById(Long spuId);

    void insertGoods(Spu spu);

    SpuDetail queryDetailById(Long spuId);

    List<Sku> querySkuListBySpuID(Long spuId);

    List<Sku> querySkuListByIds(List<Long> ids);

    void updateGoods(Spu spu);

    void onShelves(Long spuId);

    void offShelves(Long spuId);

    void deleteGoods(Long spuId);

    void decreaseStock(List<CartDTO> carts);
}
