package com.leyouxianggou.api;

import com.leyouxianggou.item.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BrandApi {
    @GetMapping("/brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);
}
