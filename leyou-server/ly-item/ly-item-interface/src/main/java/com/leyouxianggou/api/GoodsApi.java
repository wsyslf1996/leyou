package com.leyouxianggou.api;

import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Sku;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.item.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                   @RequestParam(value = "saleable",required = false) Boolean saleable,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "rows",defaultValue = "5") int pageSize);

    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long spuId);

    @GetMapping("/sku/list")
    List<Sku> querySkuListBySpuID(@RequestParam("id")Long spuId);

    @GetMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id")Long spuId);
}
