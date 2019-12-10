package com.leyouxianggou.item.service.impl;

import com.leyouxianggou.item.mapper.CategoryMapper;
import com.leyouxianggou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
}
