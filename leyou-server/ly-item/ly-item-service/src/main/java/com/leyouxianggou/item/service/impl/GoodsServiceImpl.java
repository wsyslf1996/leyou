package com.leyouxianggou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.common.vo.PageResult;
import com.leyouxianggou.item.Category;
import com.leyouxianggou.item.Spu;
import com.leyouxianggou.item.mapper.SpuMapper;
import com.leyouxianggou.item.service.BrandService;
import com.leyouxianggou.item.service.CategoryService;
import com.leyouxianggou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Override
    public PageResult<Spu> querySpuByPage(String key, Boolean saleable, int page, int pageSize) {
        // 分页
        PageHelper.startPage(page,pageSize);

        // 过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //关键字过滤
        if(StringUtils.isNotBlank(key)){
             criteria.andLike("title","%"+key+"%");
        }

        // 是否上架过滤
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //排序
        example.setOrderByClause("last_update_time DESC");

        // 查询
        List<Spu> list = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        // 解析cid1,cid2,cid3,brand_id这些字段
        list.forEach(spu -> {
            // 解析cid1,cid2,cid3 这几个字段的值，设置cname值为 手机/手机/手机 这种格式
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));

            // 解析brand_id这个字段的值
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        });

        // 封装页面信息
        PageInfo<Spu> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(),list);
    }
}
