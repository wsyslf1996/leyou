package com.leyouxianggou.item.mapper;

import com.leyouxianggou.item.Brand;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    List<Brand> queryBrandByPage(Integer page, Integer pageSize, Boolean desc, String sortBy, String key);
}
