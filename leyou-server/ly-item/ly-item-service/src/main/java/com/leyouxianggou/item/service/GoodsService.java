package com.leyouxianggou.item.service;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Spu;

public interface GoodsService {
    PageResult<Spu> querySpuByPage(String key, Boolean saleable, int page, int pageSize);
}
