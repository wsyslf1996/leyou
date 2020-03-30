package com.leyouxianggou.api;

import com.leyouxianggou.item.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {
    @GetMapping("/category/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids")List<Long> ids);
}
