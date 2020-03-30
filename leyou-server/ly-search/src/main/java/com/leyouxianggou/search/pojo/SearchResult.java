package com.leyouxianggou.search.pojo;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResult extends PageResult<Goods> {
    private List<Category> categories;

    private List<Brand> brands;

    private List<Map<String,Object>> specs;

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands,List<Map<String,Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
