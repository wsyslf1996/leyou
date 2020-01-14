package com.leyouxianggou.item.controller;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                                          @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                          @RequestParam(value = "page",defaultValue = "1") int page,
                                                          @RequestParam(value = "rows",defaultValue = "5") int pageSize){
        return ResponseEntity.ok(goodsService.querySpuByPage(key,saleable,page,pageSize));
    }

//    @GetMapping("/spu/detail/{id}")
//    public ResponseEntity<String> querySpuDetailBySpuId(@PathVariable("id")Long id){
//        return null;
//    }
}
