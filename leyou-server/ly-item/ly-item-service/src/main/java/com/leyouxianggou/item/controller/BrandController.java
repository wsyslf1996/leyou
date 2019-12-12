package com.leyouxianggou.item.controller;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("品牌服务")
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "分页查询品牌信息,可按关键字过滤查询，按字段排序，升序或者降序",notes = "查询品牌信息",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "当前页",type = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name = "rows",value = "每页显示数据条数",type = "Integer",defaultValue = "5"),
            @ApiImplicitParam(name = "desc",value = "是否降序",type = "Boolean",defaultValue = "false"),
            @ApiImplicitParam(name = "sortBy",value = "排序字段",type = "Integer",required = false),
            @ApiImplicitParam(name = "key",value = "搜索过滤关键字",type = "String",required = false)})
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
