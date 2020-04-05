package com.leyouxianggou.item.mapper;

import com.leyouxianggou.item.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku,Long> {
}
