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
    @GetMapping("/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

    @PostMapping
    public ResponseEntity<Category> insertCategory(Category category){
        categoryService.insertCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestParam("id")Long id,@RequestParam("name")String name){
        categoryService.updateCategory(id,name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id")Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
