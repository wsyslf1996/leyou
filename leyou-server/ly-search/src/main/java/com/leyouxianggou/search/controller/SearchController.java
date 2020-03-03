package com.leyouxianggou.search.controller;

import com.leyouxianggou.search.pojo.SearchRequest;
import com.leyouxianggou.search.pojo.SearchResult;
import com.leyouxianggou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        return  ResponseEntity.ok(searchService.searchGoods(searchRequest));
    }
}
