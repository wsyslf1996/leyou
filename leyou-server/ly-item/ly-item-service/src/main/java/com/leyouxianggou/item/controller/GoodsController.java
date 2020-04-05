package com.leyouxianggou.item.controller;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Sku;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.item.SpuDetail;
import com.leyouxianggou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(goodsService.querySpuById(spuId));
    }

    @PostMapping("/goods")
    public ResponseEntity<Void> insertGoods(@RequestBody Spu spu){
        goodsService.insertGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/goods/{spuId}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("spuId")Long spuId){
        goodsService.deleteGoods(spuId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id")Long spuId){
        return ResponseEntity.ok(goodsService.queryDetailById(spuId));
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuListBySpuID(@RequestParam("id")Long spuId){
        return ResponseEntity.ok(goodsService.querySkuListBySpuID(spuId));
    }

    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuListByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(goodsService.querySkuListByIds(ids));
    }

    @PostMapping("/spu/onshelves")
    public ResponseEntity<Void> onShelves(@RequestParam("spuId")Long spuId){
        goodsService.onShelves(spuId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/spu/offshelves")
    public ResponseEntity<Void> offShelves(@RequestParam("spuId")Long spuId){
        goodsService.offShelves(spuId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
