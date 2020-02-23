package com.leyouxianggou.search.service;

import com.leyouxianggou.item.Spu;
import com.leyouxianggou.search.pojo.Goods;
import com.leyouxianggou.search.pojo.SearchRequest;
import com.leyouxianggou.search.pojo.SearchResult;

public interface SearchService {
    Goods buildGoods(Spu spu);

    SearchResult searchGoods(SearchRequest searchRequest);
}
