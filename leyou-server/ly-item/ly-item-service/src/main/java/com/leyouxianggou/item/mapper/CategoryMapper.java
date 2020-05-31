package com.leyouxianggou.item.mapper;

import com.leyouxianggou.item.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {

    @Select("SELECT * from tb_category a WHERE a.id IN (SELECT category_id from tb_category_brand b WHERE b.brand_id = #{bid})")
    List<Category> queryCategoryByBid(@Param("bid") Long bid);

    @Select("SELECT COUNT(*) FROM `tb_category` where parent_id = " +
            "(SELECT parent_id from `tb_category` where id = #{id})")
    int queryBrotherCount(Long id);
}
