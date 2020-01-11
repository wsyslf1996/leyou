package com.leyouxianggou.item.controller;

import com.leyouxianggou.item.Category;
import com.leyouxianggou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam("pid") Long pid){
        return ResponseEntity.ok(categoryService.queryCategoryByPid(pid));
    }
    @GetMapping("/bid/{bid}")
    public ResponseEntity<List<Category>> queryCategoryByBid(@PathVariable("bid")Long bid){
        return ResponseEntity.ok(categoryService.queryCategoryByBid(bid));
    }

    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam("id")Long id,@RequestParam("name")String name){
        categoryService.updateCategory(id,name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
