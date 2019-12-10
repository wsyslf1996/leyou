package com.leyouxianggou.item.service.impl;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.item.Category;
import com.leyouxianggou.item.mapper.CategoryMapper;
import com.leyouxianggou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryByPid(Long pid) {
        Category category =new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)) throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        return list;
    }
}
