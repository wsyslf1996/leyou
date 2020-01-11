package com.leyouxianggou.item.service;

import com.leyouxianggou.item.SpecGroup;
import com.leyouxianggou.item.SpecParam;

import java.util.List;

public interface SpecificationService {
    List<SpecGroup> querySpecGroupByCid(Long cid);

    List<SpecParam> querySpecParamByGid(Long gid);

    void insertSpecGroup(SpecGroup specGroup);

    void updateSpecGroup(SpecGroup specGroup);
}
