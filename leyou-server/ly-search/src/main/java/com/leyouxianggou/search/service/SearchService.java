package com.leyouxianggou.search.service;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.search.pojo.Goods;
import com.leyouxianggou.search.pojo.SearchRequest;

public interface SearchService {
    public Goods buildGoods(Spu spu);

    PageResult<Goods> searchGoods(SearchRequest searchRequest);
}
