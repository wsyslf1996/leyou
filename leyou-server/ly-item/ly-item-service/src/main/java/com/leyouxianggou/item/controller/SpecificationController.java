package com.leyouxianggou.item.controller;

import com.leyouxianggou.item.SpecGroup;
import com.leyouxianggou.item.SpecParam;
import com.leyouxianggou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable("cid")Long cid){
        return ResponseEntity.ok(specificationService.querySpecGroupByCid(cid));
    }

    @PostMapping("/group")
    public ResponseEntity<Void> insertSpecGroup(SpecGroup specGroup){
        specificationService.insertSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/group")
    public ResponseEntity<Void> updateSpecGroup(SpecGroup specGroup){
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id")Long id){
        specificationService.deleteSpecGroup(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamByGid(@RequestParam("gid")Long gid){
        return ResponseEntity.ok(specificationService.querySpecParamByGid(gid));
    }

    @PostMapping("/param")
    public ResponseEntity<Void> insertSpecParam(SpecParam specParam){
        specificationService.insertSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/param")
    public ResponseEntity<Void> updateSpecParam(SpecParam specParam){
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id")Long id){
        specificationService.deleteSpecParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
