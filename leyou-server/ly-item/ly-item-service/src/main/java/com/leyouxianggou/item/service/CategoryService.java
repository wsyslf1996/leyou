package com.leyouxianggou.item.service;

import com.leyouxianggou.item.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryCategoryByPid(Long pid);
}
