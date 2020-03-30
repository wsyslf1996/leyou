package com.leyouxianggou.api;

import com.leyouxianggou.item.SpecGroup;
import com.leyouxianggou.item.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    @GetMapping("/spec/params")
    List<SpecParam> querySpecParamList(@RequestParam(value = "gid",required = false)Long gid,
                                       @RequestParam(value = "cid",required = false)Long cid,
                                       @RequestParam(value = "searching",required = false)Boolean searching);

    @GetMapping("/spec/group")
    List<SpecGroup> queryGroupAndParams(@RequestParam("cid")Long cid);
}
