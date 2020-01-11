package com.leyouxianggou.item.service.impl;

import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.item.SpecGroup;
import com.leyouxianggou.item.SpecParam;
import com.leyouxianggou.item.mapper.SpecGroupMapper;
import com.leyouxianggou.item.mapper.SpecParamMapper;
import com.leyouxianggou.item.service.SpecificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Slf4j
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(list)){
            log.info("==============商品规格组参数未查询到===============");
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    @Override
    public void insertSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.insertSelective(specGroup);
        if(count!=1){
            throw new LyException(ExceptionEnum.SPEC_GROUP_INSERT_ERROR);
        }
    }

    @Override
    public void updateSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKeySelective(specGroup);
        if(count!=1){
            throw new LyException(ExceptionEnum.SPEC_GROUP_INSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteSpecGroup(Long id) {

        // 删除一个分组下的所有参数信息
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(id);
        specParamMapper.delete(specParam);
        // 删除一个分组
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        specGroupMapper.deleteByPrimaryKey(specGroup);
    }

    @Override
    public List<SpecParam> querySpecParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)){
            log.info("==============商品规格参数未查询到===============");
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    @Override
    public void insertSpecParam(SpecParam specParam) {
        int count = specParamMapper.insert(specParam);
        if(count!=1){
            throw new LyException(ExceptionEnum.SPEC_PARAM_INSERT_ERROR);
        }
    }

    @Override
    public void updateSpecParam(SpecParam specParam) {
        int count = specParamMapper.updateByPrimaryKeySelective(specParam);
        if(count!=1){
            throw new LyException(ExceptionEnum.SPEC_PARAM_INSERT_ERROR);
        }
    }

    @Override
    public void deleteSpecParam(Long id) {
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        specParamMapper.deleteByPrimaryKey(specParam);
    }
}
