package com.leyouxianggou.item.service;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.Category;

import java.util.List;

public interface BrandService {
    PageResult<Brand> queryBrandByPage(Integer page, Integer pageSize, Boolean desc, String sortBy, String key);

    void insertBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> cids);

    void deleteBrand(Long id);

    Brand queryById(Long id);

    List<Brand> queryBrandByCid(Long cid);
}
