package com.leyouxianggou.item.controller;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                              @RequestParam(value = "rows",defaultValue = "5")Integer pageSize,
                                                              @RequestParam(value = "desc",defaultValue = "false")Boolean desc,
                                                              @RequestParam(value = "sortBy",required = false)String sortBy,
                                                              @RequestParam(value = "key",required = false)String key){
        return ResponseEntity.ok(brandService.queryBrandByPage(page,pageSize,desc,sortBy,key));

    }

    @PostMapping
    public ResponseEntity<Void> insertBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        brandService.insertBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
