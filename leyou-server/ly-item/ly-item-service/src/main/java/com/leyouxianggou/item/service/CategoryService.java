package com.leyouxianggou.item.service;

import com.leyouxianggou.item.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryCategoryByPid(Long pid);

    List<Category> queryCategoryByBid(Long bid);

    void updateCategory(Long id, String name);

    void insertCategory(Category category);

    void deleteCategory(Long id);

    List<Category> queryByIds(List<Long> ids);
}
