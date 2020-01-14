package com.leyouxianggou.item.service;

import com.leyouxianggou.item.SpecGroup;
import com.leyouxianggou.item.SpecParam;

import java.util.List;

public interface SpecificationService {
    List<SpecGroup> querySpecGroupByCid(Long cid);

    List<SpecParam> querySpecParamList(Long gid, Long cid, Boolean searching);

    void insertSpecGroup(SpecGroup specGroup);

    void updateSpecGroup(SpecGroup specGroup);

    void deleteSpecGroup(Long id);

    void insertSpecParam(SpecParam specParam);

    void updateSpecParam(SpecParam specParam);

    void deleteSpecParam(Long id);

}
