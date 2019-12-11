package com.leyouxianggou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.mapper.BrandMapper;
import com.leyouxianggou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌信息
     *
     * @param page     当前页
     * @param pageSize 每页数据条数
     * @param desc     是否降序
     * @param sortBy   排序字段
     * @param key      搜索关键字(可按品牌字母和品牌名称搜索)
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer pageSize, Boolean desc, String sortBy, String key) {
        //先分页
        PageHelper.startPage(page, pageSize);


        Example example = new Example(Brand.class);
        //过滤
        if (StringUtils.isNotBlank(key)) {
            //设置过滤条件，sql: where name like '%key%' or letter == key
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }

        //排序
        //设置排序子句，sql: id DESC，example.setOrderByClause()方法会自动拼接到sql后面
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "DESC" : "ASC"));
        }

        //查询
        List<Brand> list = brandMapper.selectByExample(example); //这个brands是分页后的结果
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        //将结果封装到PageResult中
        PageInfo<Brand> pageInfo = new PageInfo<>(list);

        return new PageResult<>(pageInfo.getTotal(), list);
    }
}
