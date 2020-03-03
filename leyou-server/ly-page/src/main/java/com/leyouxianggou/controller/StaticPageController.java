package com.leyouxianggou.controller;

import com.leyouxianggou.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
public class StaticPageController {

    @Autowired
    private StaticPageService staticPageService;

    @GetMapping("/item/{id}.html")
    public String goodsDetailPage(@PathVariable("id")Long spuId, Model model){
        Map<String,Object> map = staticPageService.goodsDetailPage(spuId);
        model.addAllAttributes(map);
        return "item";
    }

    @GetMapping("createHtml/{id}")
    public ResponseEntity<Void> createHtml(@PathVariable("id")Long spuId){
        staticPageService.createHtml(spuId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
