package com.leyouxianggou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Brand;
import com.leyouxianggou.item.Category;
import com.leyouxianggou.item.mapper.BrandMapper;
import com.leyouxianggou.item.mapper.CategoryMapper;
import com.leyouxianggou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * @param key      过滤搜索关键字(可按品牌字母和品牌名称搜索)
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
        //sort by id desc,sort by 会自动生成
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

    /**
     * 新增品牌信息
     * @param brand
     * @param cids
     */
    @Override
    @Transactional
    public void insertBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if(count!=1) throw new LyException(ExceptionEnum.BRAND_INSERT_ERROR);
        Long bid = brand.getId();
        //新增品牌种类中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid,bid);
            if(count!=1) throw new LyException(ExceptionEnum.BRAND_INSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        Long bid = brand.getId();
        //修改品牌逻辑梳理，如果传过来的cids没有变动，则不用管中间表。否则再新增或删除。
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.deleteCategoryBrandByBid(bid);
        cids.forEach(cid->{
            int count = brandMapper.insertCategoryBrand(cid, bid);
            if(count !=1) throw new LyException(ExceptionEnum.BRAND_UPDATE_ERROR);
        });
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        // 删除中间表
        brandMapper.deleteCategoryBrandByBid(id);

        // 删除Brand信息
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Brand queryById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand==null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryBrandByCid(cid);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<Brand> queryByIds(List<Long> ids) {
        List<Brand> list = brandMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
}
